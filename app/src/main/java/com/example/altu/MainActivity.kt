package com.example.altu

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.altu.ChatBar.Chat.MessageComposer
import com.example.altu.ChatBar.Chat.MessageList
import com.example.altu.ChatBar.Chat.TopChatBar
import com.example.altu.ChatBar.ChatBar
import com.example.altu.ChatBar.ChatItem
import com.example.altu.DataBase.AltuDatabase
import com.example.altu.DataBase.ChatRepository
import com.example.altu.crypto.IdentityStore
import com.example.altu.NewContact.ContactBar
import com.example.altu.Profile.LocalUser
import com.example.altu.NewContact.ContactTab
import com.example.altu.NewContact.ContactTabBar
import com.example.altu.NewContact.QrFrame
import com.example.altu.NewContact.ScanFrame
import com.example.altu.R
import com.example.altu.Routes.NavBar
import com.example.altu.Routes.Routes
import com.example.altu.SearchBar.SearchBar
import com.example.altu.Settings.PrivacyScreen
import com.example.altu.Settings.Settings
import com.example.altu.SoundBar.MusicController
import com.example.altu.SoundBar.SoundBar
import com.example.altu.ui.theme.AltuTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val barScrim = Color(0xFF070809).toArgb()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(barScrim),
            navigationBarStyle = SystemBarStyle.dark(barScrim),
        )
        window.isNavigationBarContrastEnforced = false
        setContent {
            AltuTheme(darkTheme = true, dynamicColor = false) {
                Main()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val identity = remember { IdentityStore(context).loadOrCreate() }
    val repository = remember { ChatRepository(AltuDatabase.get(context), identity) }
    val chats by repository.observeChats().collectAsState(initial = emptyList())
    val musicController = remember { MusicController(context) }
    var lastOpenChatId by remember { mutableStateOf<String?>(null) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val onChat = currentRoute?.startsWith("chat/") == true
    val imeVisible = WindowInsets.isImeVisible
    val liftChatForIme = onChat && imeVisible
    val layoutDirection = LocalLayoutDirection.current

    LaunchedEffect(repository) {
        repository.seedIfEmpty()
    }

    DisposableEffect(musicController) {
        onDispose { musicController.release() }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF070809),
        bottomBar = {
            if (!liftChatForIme) {
                NavBar(
                    navController = navController,
                    unreadCount = chats.sumOf { it.unreadCount },
                    lastOpenChatId = lastOpenChatId,
                    onReturnToHomeList = { lastOpenChatId = null },
                )
            }
        },
    ) { innerPadding ->
        val hostPadding = PaddingValues(
            start = innerPadding.calculateStartPadding(layoutDirection),
            top = innerPadding.calculateTopPadding(),
            end = innerPadding.calculateEndPadding(layoutDirection),
            bottom = if (liftChatForIme) 0.dp else innerPadding.calculateBottomPadding(),
        )
        NavHost(
            navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(hostPadding).fillMaxSize().clip(RoundedCornerShape(32.dp))
        ) {
            composable(Routes.Home.route) {
                Home(
                    chats = chats,
                    musicController = musicController,
                    onChatClick = { chat ->
                        lastOpenChatId = chat.id
                        navController.navigate(Routes.Chat.create(chat.id))
                    }
                )
            }
            composable(
                route = Routes.Chat.route,
                arguments = listOf(navArgument("chatId") { type = NavType.StringType }),
            ) { entry ->
                Chat(
                    chatId = entry.arguments?.getString("chatId"),
                    repository = repository,
                    onHomeClick = {
                        navController.popBackStack(
                            route = Routes.Home.route,
                            inclusive = false,
                            saveState = true,
                        )
                        lastOpenChatId = null
                    },
                )
            }
            composable(Routes.Settings.route) {
                Settings(
                    onBackToChats = {
                        navController.navigate(Routes.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onPrivacyClick = {
                        navController.navigate(Routes.Privacy.route)
                    },
                    onDeleteAllMessages = {
                        scope.launch { repository.deleteAllMessages() }
                    },
                )
            }
            composable(Routes.Privacy.route) {
                PrivacyScreen(
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Routes.NewContact.route) {
                NewContact(
                    onBackToChats = {
                        navController.navigate(Routes.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun Home(
    chats: List<ChatItem>,
    musicController: MusicController,
    onChatClick: (ChatItem) -> Unit = {},
) {
    var query by remember { mutableStateOf("") }
    val filteredChats = remember(query, chats) {
        if (query.isBlank()) {
            chats
        } else {
            chats.filter { it.nickname.contains(query, ignoreCase = true) }
        }
    }

    Column(Modifier.fillMaxSize()) {
        SoundBar(controller = musicController)
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
        ChatBar(
            chats = filteredChats,
            onChatClick = onChatClick,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        )
    }
}

@Composable
fun Chat(
    chatId: String? = null,
    repository: ChatRepository,
    onHomeClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val chat by repository.observeChat(chatId.orEmpty()).collectAsState(initial = null)
    val messages by repository.observeMessages(chatId.orEmpty()).collectAsState(initial = emptyList())
    var messageSearchVisible by remember { mutableStateOf(false) }
    var messageQuery by remember { mutableStateOf("") }
    var findNextToken by remember { mutableIntStateOf(0) }
    var draft by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.chat_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            TopChatBar(
                nickname = chat?.nickname ?: "Chat",
                avatarRes = chat?.avatarRes ?: LocalUser.avatarRes,
                onHomeClick = onHomeClick,
                onFindNearestMessageClick = {
                    when {
                        !messageSearchVisible -> messageSearchVisible = true
                        messageQuery.isNotBlank() -> findNextToken++
                        else -> messageSearchVisible = false
                    }
                },
            )
            if (messageSearchVisible) {
                SearchBar(
                    query = messageQuery,
                    onQueryChange = { messageQuery = it },
                    onSearch = {
                        if (messageQuery.isNotBlank()) findNextToken++
                    },
                    placeholder = "Find message",
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            MessageList(
                messages = messages,
                searchQuery = if (messageSearchVisible) messageQuery else "",
                findNextToken = findNextToken,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
            )
            MessageComposer(
                text = draft,
                onTextChange = { draft = it },
                onSend = {
                    val body = draft
                    draft = ""
                    scope.launch { repository.sendMessage(chatId.orEmpty(), body) }
                },
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
fun NewContact(
    onBackToChats: () -> Unit = {},
) {
    var selectedTab by remember { mutableStateOf(ContactTab.Qr) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        ContactBar(onBackToChats = onBackToChats)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (selectedTab) {
                ContactTab.Qr -> QrFrame()
                ContactTab.Scan -> ScanFrame()
            }
        }
        ContactTabBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
        )
    }
}
