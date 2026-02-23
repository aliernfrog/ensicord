> [!WARNING]
> Ensicord Compose is still a WIP and it is currently not feature complete.
>
> 📥 If you want to see what Ensicord Compose aims to be, you can download the latest available version, which is **very outdated** and **partially functional**, but has a working Ensi, from [Releases](https://github.com/aliernfrog/ensicord/releases)
> 
> ✏️ To check the progress of the rewrite, you can grab the latest build from [GitHub Actions](https://github.com/aliernfrog/ensicord/actions/workflows/commit.yml)
>
> 📋 Check out this PR to see what is being worked on and what is finished: [#12](https://github.com/aliernfrog/ensicord/pull/12)


<div align="center">

  <img alt="Ensicord icon" src="https://aliernfrog.github.io/icons/projects/ensicord.png" width="120px"/>
  
  # Ensicord Compose (WIP)
  <a href="https://aliernfrog.github.io/ensibot">Ensi</a>, in a separate & offline app
  
  <br>

  <!-- [![Download (outdated)](https://img.shields.io/github/v/tag/aliernfrog/ensicord?style=for-the-badge&label=Download+(outdated))](https://github.com/aliernfrog/ensicord/releases) -->
  
  <i style="font-size: 0.7rem;">Ensicord Compose requires at least <b>Android 6.0</b></i>
</div>

## 🩹 Project structure
Ensicord Compose shares codebase with my other apps.
To avoid updating the codebase for each app, shared code was moved into [this](https://github.com/aliernfrog/pf-tool/tree/main/shared) module in `pf-tool` repository.

## ⚖️ License
Ensicord is licensed under the GPLv3 license.<br />
You must keep the source code public if you are distributing your own version of Ensicord. See [LICENSE.md](LICENSE.md) file for more details.

## 🔧 Building
<details>
  <summary>Using GitHub Actions</summary>

  - Fork the repository
  - Add environment variables required for signing from **Repository settings > Secrets and variables > Actions > Repository secrets**:
    - `KEYSTORE_ALIAS`
    - `KEYSTORE_BASE64` this can be obtained using `openssl base64 -in keystore.jks`
    - `KEYSTORE_PASSWORD`
    - `KEY_PASSWORD`
  - Enable workflows
  - Trigger a build workflow and wait for it to build a release variant APK
</details>
<details>
  <summary>Locally</summary>

  - Clone the repository
  - Add a signing config (unless you only want to build debug variant or sign manually)
  - Build APK:
    - Release variant: `./gradlew assembleRelease`
    - Debug variant: `./gradlew assembleDebug`
</details>
