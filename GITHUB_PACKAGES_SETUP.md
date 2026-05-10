# GitHub Packages Publishing Setup

## Overview
Your MoviesService library is now configured to publish to GitHub Packages. This provides a private Maven repository for your library.

## Current Configuration

### ✅ What's Configured
- **Repository**: `https://maven.pkg.github.com/Combonary/MoviesService`
- **Group ID**: `io.github.combonary`
- **Artifact ID**: `MoviesService`
- **Version**: `1.0.2`
- **Credentials**: GitHub username and personal access token

### 📦 Publishing Status
- ✅ Build successful
- ✅ Publishing successful
- ✅ All KMP targets published (Android, iOS, JVM, Linux)

## How to Use Your Library

### For Team Members / Private Use

Add this to your project's `build.gradle.kts` or `settings.gradle.kts`:

```kotlin
// In settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/Combonary/MoviesService")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: "YOUR_GITHUB_USERNAME"
                password = System.getenv("GITHUB_TOKEN") ?: "YOUR_GITHUB_TOKEN"
            }
        }
        // ... other repositories
    }
}

// In app/build.gradle.kts
dependencies {
    implementation("io.github.combonary:MoviesService:1.0.2")
}
```

### For CI/CD (GitHub Actions)

```yaml
# .github/workflows/publish.yml
name: Publish Library
on:
  push:
    tags:
      - 'v*'

jobs:
  publish:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Publish to GitHub Packages
        run: ./gradlew publish
        env:
          GITHUB_ACTOR: ${{ github.actor }}
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

## Publishing Commands

### Publish to GitHub Packages
```bash
./gradlew publish
```

### Publish to Local Maven (Testing)
```bash
./gradlew publishToMavenLocal
```

### Check Available Tasks
```bash
./gradlew tasks --group=publishing
```

## Repository Structure

After publishing, your library will be available at:
```
https://maven.pkg.github.com/Combonary/MoviesService/io/github/combonary/MoviesService/1.0.2/
```

Published artifacts include:
- `MoviesService-1.0.2.jar` (Kotlin Multiplatform)
- `MoviesService-1.0.2-sources.jar` (Source code)
- `MoviesService-1.0.2.module` (Gradle metadata)
- `MoviesService-1.0.2.pom` (Maven POM)

## Managing Versions

### Update Version
Edit `library/build.gradle.kts`:
```kotlin
version = "1.0.3"  // Increment for new releases
```

### Version Strategy
- **Major.Minor.Patch** (e.g., 1.0.2)
- **Snapshot versions**: `1.0.3-SNAPSHOT` (for development)

## Troubleshooting

### Authentication Issues
```bash
# Check your token has these permissions:
# - repo (full control)
# - packages: read/write
```

### 403 Forbidden
- Ensure you're using a GitHub Personal Access Token (not password)
- Token must have `packages: write` permission
- Repository must exist and be accessible

### 404 Not Found
- Check group ID: `io.github.combonary`
- Check repository URL: `https://maven.pkg.github.com/Combonary/MoviesService`

### Build Issues
```bash
# Clean and rebuild
./gradlew clean build

# Check for errors
./gradlew build --stacktrace
```

## Security Notes

### Credentials
- **Never commit tokens** to Git
- Use environment variables in CI/CD
- Rotate tokens regularly
- Use fine-grained tokens when possible

### Repository Access
- GitHub Packages respects repository visibility
- Private repositories require authentication
- Public repositories can be accessed without auth (but still need for publishing)

## Alternative Publishing Options

If you want to make your library publicly available, consider:

### Maven Central (Sonatype OSSRH)
- Maximum discoverability
- No authentication needed for downloads
- Requires GPG signing and approval process

### JitPack
- Zero configuration
- Automatic publishing from git tags
- Good for quick sharing

## Next Steps

1. **Test Integration**: Try using your library in another project
2. **Documentation**: Create usage examples and API docs
3. **CI/CD**: Set up automated publishing on releases
4. **Version Management**: Plan your versioning strategy

## Support

- **GitHub Packages Docs**: https://docs.github.com/en/packages
- **Kotlin Multiplatform**: https://kotlinlang.org/docs/multiplatform.html
- **Gradle Publishing**: https://docs.gradle.org/current/userguide/publishing_maven.html

---

**Your library is ready to use!** 🎉

Team members can now add:
```kotlin
implementation("io.github.combonary:MoviesService:1.0.2")
```

