# HIKARI BANK 公開プレビュー（閲覧専用）

元の銀行プロジェクトとは**別フォルダ**で管理すること。銀行口座データや元のBank.javaを、この公開リポジトリに入れないこと。

## Mac ローカル確認
`sh start-preview.sh` で起動。 http://localhost:8080 （既存8080サーバーは停止する）。

## Renderへのデプロイ
1. このフォルダ**だけ**をGitHubの新しい非公開リポジトリへ保存。
2. Renderで New > Web Service > Git Provider を選び、リポジトリを選択。
3. Runtime: Docker（Dockerfileを自動認識する場合はそのまま）。
4. デプロイ完了後、RenderのHTTPS URLを開く。サービスのプランによってスリープや料金が異なる。

## セキュリティ
APIなし、口座操作なし、実口座データなし、管理者認証なし。これは見た目のみのプレビューであり、銀行システム本番版ではない。
既存の `Bank.java` / `bank_data.txt` / `script.js` はこの公開用フォルダにコピーしないこと。
