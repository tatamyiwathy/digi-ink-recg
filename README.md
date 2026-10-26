# OnsenTamago (Digital Ink Recognition Sample)

`OnsenTamago` は、Google ML Kitの **Digital Ink Recognition（デジタルインク認識）API** を活用した、Android向けの手書き文字認識サンプルアプリです。

## 🚀 主な機能

- **手書きキャンバス**: 指やスタイラスペンで画面上に自由に文字や図形を描画できます。
- **オンデバイス認識**: ML Kitを使用して、描画されたストロークを高精度かつリアルタイムにテキストや形状に変換します。
- **モデル管理**: 認識に必要な言語モデル（日本語・英語など）のダウンロードおよび管理を行います。

## 🛠️ 技術スタック

- **言語**: Kotlin
- **UI**: Jetpack Compose, Material 3
- **アーキテクチャ**: Declarative UI
- **主要な依存関係**:
    - `com.google.mlkit:digital-ink-recognition` (v18.1.0)
    - AndroidX Core, Lifecycle, Activity Compose

## 📱 動作環境

- **Min SDK**: 24以上
- **Target SDK**: 37
- **開発環境**: Android Studio (最新版推奨)

## ⚙️ ビルド・実行方法

1. このリポジトリをクローンまたはダウンロードします。
2. Android Studioでプロジェクトを開きます。
3. Gradleの同期（Sync Project with Gradle Files）を行います。
4. Androidデバイスまたはエミュレータ（Android 7.0 / API 24以上）で実行します。

## 📖 使い方

1. アプリを起動すると手書き用のキャンバスが表示されます。
2. キャンバス上に文字や数字、記号などを手書きで描きます。
3. 認識結果が画面上にリアルタイム（またはボタン押下時）に反映されます。

## 🔗 参考リンク

- [Google ML Kit Digital Ink Recognition 公式ドキュメント](https://developers.google.com/ml-kit/vision/digital-ink-recognition)
- [Jetpack Compose ドキュメント](https://developer.android.com/jetpack/compose)

- ## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
