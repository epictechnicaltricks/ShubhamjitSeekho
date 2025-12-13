# 🎬 Anime App - Netflix Style Android Application

A modern Android application built with Kotlin that displays top anime using the Jikan API. Features a Netflix-inspired dark UI with offline-first architecture.

## 📱 Screenshots


## ✨ Features

- 🎨 **Netflix-Style Dark UI** - Modern, sleek interface with gradient backgrounds
- 📶 **Offline-First Architecture** - Works without internet after initial load
- 🔄 **Pull-to-Refresh** - Swipe down to refresh anime list
- 🎥 **Video Trailers** - Watch anime trailers directly in the app
- ⭐ **Ratings & Info** - View scores, episodes, genres, and synopsis
- 🚀 **Fast Loading** - Cached data loads instantly
- 📱 **Responsive Grid Layout** - Beautiful 2-column card layout

## 🛠️ Tech Stack

### Architecture
- **MVVM** (Model-View-ViewModel)
- **Clean Architecture** (Domain, Data, UI layers)
- **Offline-First** with Repository pattern

### Libraries & Dependencies
- **Kotlin** - Programming language
- **Coroutines** - Asynchronous programming
- **Flow** - Reactive data streams
- **Retrofit** - REST API client
- **Room** - Local database
- **Glide** - Image loading
- **ExoPlayer** - Video playback
- **ViewBinding** - Type-safe view access
- **Material Design** - UI components

## 📂 Project Structure

```
app/src/main/java/com/shubhamjitseekho/
│
├── ui/                          # Presentation Layer
│   ├── common/
│   │   └── UiState.kt          # State management
│   ├── home/
│   │   ├── HomeActivity.kt     # Main screen
│   │   ├── HomeViewModel.kt    # Business logic
│   │   └── AnimeListAdapter.kt # RecyclerView adapter
│   └── detail/
│       ├── DetailActivity.kt   # Detail screen
│       └── DetailViewModel.kt  # Detail logic
│
├── domain/                      # Business Logic Layer
│   ├── model/
│   │   ├── Anime.kt           # Anime model
│   │   └── AnimeDetail.kt     # Detail model
│   └── repository/
│       └── AnimeRepository.kt  # Repository interface
│
└── data/                        # Data Layer
    ├── remote/
    │   ├── JikanApi.kt         # API interface
    │   ├── RetrofitClient.kt   # Retrofit setup
    │   └── dto/                # API response models
    ├── local/
    │   ├── AppDatabase.kt      # Room database
    │   ├── dao/
    │   │   └── AnimeDao.kt     # Database queries
    │   └── entity/
    │       └── AnimeEntity.kt  # Database table
    └── repository/
        └── AnimeRepositoryImpl.kt # Repository implementation
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17 or newer
- Android SDK 24 or higher
- Internet connection (for initial data fetch)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/anime-app.git
cd anime-app
```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Wait for Gradle sync to complete
   - All dependencies will be downloaded automatically

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button (▶️) or press `Shift + F10`

## 📦 Dependencies

Add to your `libs.versions.toml`:

```toml
[versions]
kotlin = "1.9.0"
retrofit = "2.9.0"
room = "2.6.0"
glide = "4.16.0"
exoplayer = "2.19.1"
coroutines = "1.7.3"

[libraries]
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "retrofit" }
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version = "4.11.0" }

room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

glide = { group = "com.github.bumptech.glide", name = "glide", version.ref = "glide" }
exoplayer = { group = "com.google.android.exoplayer", name = "exoplayer", version.ref = "exoplayer" }

coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "coroutines" }
coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
```

## 🌐 API

This app uses the [Jikan API](https://jikan.moe/) - Unofficial MyAnimeList API

**Base URL:** `https://api.jikan.moe/v4/`

**Endpoints Used:**
- `GET /top/anime` - Get top anime list
- `GET /anime/{id}` - Get anime details

**Rate Limits:**
- 3 requests per second
- 60 requests per minute

## 🏗️ Architecture

### Offline-First Flow

```
UI (Activity)
    ↓ observes StateFlow
ViewModel
    ↓ calls
Repository
    ↓ emits Room data immediately
    ↓ fetches API in background
    ↓ updates Room
    ↓ Room Flow auto-emits
UI updates automatically
```

### Data Flow

1. **User opens app** → Shows cached data instantly
2. **Background API fetch** → Gets fresh data
3. **Update database** → Room saves new data
4. **UI auto-updates** → Flow emits new data

## 🎨 UI Components

### Home Screen
- Grid layout with 2 columns
- Anime cards with:
  - Poster image
  - Title
  - Rating (⭐)
  - Episode count
- Pull-to-refresh functionality
- Loading states with progress bar
- Error handling with retry

### Detail Screen
- Hero image
- Video trailer player (if available)
- Anime title and ratings
- Episode count and status
- Genres as chips
- Expandable synopsis
- Back navigation

## 🔧 Configuration

### Change Theme Colors

Edit `res/values/colors.xml`:

```xml
<color name="netflix_red">#E50914</color>
<color name="netflix_black">#141414</color>
<color name="card_background">#1F1F1F</color>
```

### Adjust Cache Timeout

Edit `AnimeRepositoryImpl.kt`:

```kotlin
companion object {
    private const val CACHE_TIMEOUT = 3600000L // 1 hour
}
```

### Change Grid Columns

Edit `HomeActivity.kt`:

```kotlin
layoutManager = GridLayoutManager(this@HomeActivity, 2) // Change 2 to desired columns
```

## 🐛 Troubleshooting

### Issue: Gradle Sync Failed
**Solution:** 
- Check internet connection
- Update Android Studio
- Invalidate Caches: `File → Invalidate Caches / Restart`

### Issue: API Rate Limit Exceeded
**Solution:**
- Wait 1 minute
- App automatically retries
- Uses cached data meanwhile

### Issue: Images Not Loading
**Solution:**
- Check internet connection
- Verify Glide dependency
- Check image URL in logs

### Issue: Room Database Error
**Solution:**
- Uninstall and reinstall app
- Clear app data
- Check entity version number

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Jikan API](https://jikan.moe/) - Anime data provider
- [MyAnimeList](https://myanimelist.net/) - Original data source
- Netflix - UI/UX inspiration

## 👨‍💻 Developer

**Shubham Jit Seekho**
- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 Changelog

### Version 1.0.0 (2024-01-XX)
- Initial release
- Netflix-style UI
- Offline-first architecture
- Top anime list
- Anime details with trailer
- Pull-to-refresh
- Room database caching

## 🔮 Future Enhancements

- [ ] Search functionality
- [ ] Favorites/Watchlist
- [ ] User authentication
- [ ] Multiple anime categories
- [ ] Dark/Light theme toggle
- [ ] Pagination for anime list
- [ ] Filter by genre
- [ ] Sort options (rating, popularity, etc.)
- [ ] Share anime with friends
- [ ] Episode tracking

## 📊 Performance

- **App Size:** ~15 MB
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Initial Load:** < 2 seconds (with cache)
- **API Response:** ~500ms average

## 🔒 Privacy

This app:
- ✅ Does NOT collect personal data
- ✅ Does NOT require user registration
- ✅ Uses only public API data
- ✅ Stores data locally on device
- ✅ No analytics or tracking

---

Made with ❤️ by Shubham Jit Seekho
