package com.example.altu.api

import com.example.altu.api.dto.QueuedMessageDto
import com.example.altu.crypto.Ed25519Identity
import com.example.altu.crypto.SendEnvelope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.util.Base64
import java.util.concurrent.TimeUnit

class AltuApi(
    private val config: ServerConfig = ServerConfig(),
    private val http: OkHttpClient = defaultClient(),
) {
    suspend fun register(userId: String, tag: String, publicKey: ByteArray) {
        val publicKeyB64 = Base64.getEncoder().encodeToString(publicKey)
        val body = buildString {
            append('{')
            append("\"userId\":").append(JsonText.string(userId)).append(',')
            append("\"tag\":").append(JsonText.string(tag)).append(',')
            append("\"publicKey\":").append(JsonText.string(publicKeyB64))
            append('}')
        }
        execute(
            Request.Builder()
                .url(url(ServerPaths.REGISTER))
                .post(body.toJsonBody())
                .build(),
        )
    }

    suspend fun send(envelope: SendEnvelope) {
        val signed = SignedHeaders.fromEnvelope(envelope)
        execute(
            Request.Builder()
                .url(url(ServerPaths.SEND))
                .post(envelope.rawBody.toJsonBody())
                .header(SendEnvelope.TIMESTAMP_HEADER, signed.timestampMillis.toString())
                .header(SendEnvelope.SIGNATURE_HEADER, signed.signatureBase64)
                .build(),
        )
    }

    suspend fun fetchMessages(userId: String, identity: Ed25519Identity): List<QueuedMessageDto> {
        val payload = ServerPaths.getMessagesPayload(userId)
        val signed = SignedHeaders.ofBody(identity, payload)
        val text = execute(
            Request.Builder()
                .url(url(ServerPaths.messages(userId)))
                .get()
                .header(SendEnvelope.TIMESTAMP_HEADER, signed.timestampMillis.toString())
                .header(SendEnvelope.SIGNATURE_HEADER, signed.signatureBase64)
                .build(),
        )
        return parseMessages(text)
    }

    suspend fun ackReceived(userId: String, messageIds: List<String>, identity: Ed25519Identity) {
        if (messageIds.isEmpty()) return
        val body = buildString {
            append('{')
            append("\"userId\":").append(JsonText.string(userId)).append(',')
            append("\"messageIds\":").append(JsonText.stringArray(messageIds))
            append('}')
        }
        val signed = SignedHeaders.ofBody(identity, body)
        execute(
            Request.Builder()
                .url(url(ServerPaths.RECEIVED))
                .patch(body.toJsonBody())
                .header(SendEnvelope.TIMESTAMP_HEADER, signed.timestampMillis.toString())
                .header(SendEnvelope.SIGNATURE_HEADER, signed.signatureBase64)
                .build(),
        )
    }

    private fun url(path: String): String {
        return config.baseUrl.trimEnd('/') + path
    }

    private suspend fun execute(request: Request): String = withContext(Dispatchers.IO) {
        http.newCall(request).execute().use { response ->
            val text = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw AltuApiException(
                    message = text.ifBlank { "HTTP ${response.code}" },
                    statusCode = response.code,
                )
            }
            text
        }
    }

    private fun parseMessages(raw: String): List<QueuedMessageDto> {
        if (raw.isBlank() || raw == "[]") return emptyList()
        val array = JSONArray(raw)
        return buildList {
            for (index in 0 until array.length()) {
                val item = array.getJSONObject(index)
                add(
                    QueuedMessageDto(
                        messageId = item.getString("messageId"),
                        fromUserId = item.getString("fromUserId"),
                        toUserId = item.getString("toUserId"),
                        chatId = item.getString("chatId"),
                        nonce = item.getString("nonce"),
                        ciphertext = item.getString("ciphertext"),
                        createdAt = item.optString("createdAt"),
                    ),
                )
            }
        }
    }

    companion object {
        private val JSON = "application/json; charset=utf-8".toMediaType()

        private fun defaultClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
        }

        private fun String.toJsonBody() = toRequestBody(JSON)
    }
}
