
# よみちゃん
新参ディスコード読み上げボットです、特長はかわいいです！

## ボットコマンド集や使い方はこちらへ
[押して](https://github.com/yakisalmon/yomichan/wiki/%E3%82%B3%E3%83%9E%E3%83%B3%E3%83%89%E9%9B%86%EF%BC%86%E4%BD%BF%E3%81%84%E6%96%B9)

## 詳細
テキストチャンネルのメッセージを元に音声合成で読み上げます。デフォルトの読み上げエンジンはVOICEVOXを使ってます。Voiceroidや棒読みちゃんなどに切り替えることは可能です。

## インストール
### 下準備
1. Javaをインストール
2. [ディスコードボットTokenの取得](https://github.com/yakisalmon/yomichan/wiki/%E3%83%87%E3%82%A3%E3%82%B9%E3%82%B3%E3%83%BC%E3%83%89%E3%83%9C%E3%83%83%E3%83%88Token%E3%81%AE%E5%8F%96%E5%BE%97%E6%96%B9%E6%B3%95)
### インストール
#### ウインドーズ
1. ボットのために任意のところにフォルダーを作ってください（権限いらないところで）
2. フォルダーの中でShift＋右クリでPowershellを開く
	3. できない場合はフォルダーパスをコピーして、スタートメニューからPowershellを開く、次のコマンドでフォルダーへ移動します ```cd C:\path\to\your\folder```
4. 以下のコマンドを実行して、必要なファイルをダウンロードします
```
# ボットサーバーのファイルをダウンロードして、解凍します
Invoke-WebRequest -Uri https://github.com/yakisalmon/yomichan/releases/download/v1.0.3-alpha/yomichan.zip -OutFile yomichan.zip

Expand-Archive yomichan.zip -DestinationPath .

# VOICEVOXをダウンロードして　解凍します
Invoke-WebRequest -Uri https://github.com/VOICEVOX/voicevox/releases/download/0.9.3/voicevox-cpu-0.9.3-x64.nsis.7z.0 -OutFile voicevox-cpu.7z

.\7za.exe x voicevox-cpu.7z -y

# VOICEVOXサーバーを起動します
start run.exe

# 15秒を待って
timeout 15

# ボットサーバーを起動します
java -jar yomichan.jar

さっきダウンロードした、もういらないファイルをデリートします
rm yomichan.zip
rm voicevox-cpu.7z
```
#### Linux (準備中)

## 特別感謝
### [VOICEVOX](https://voicevox.hiroshiba.jp/)
VOICEVOX:四国めたん
VOICEVOX:ずんだもん
VOICEVOX:春日部つむぎ
VOICEVOX:波音リツ
