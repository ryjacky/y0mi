
# よみちゃん
新参ディスコード読み上げボットです、特長はかわいいです！

## 詳細
テキストチャンネルのメッセージを元に音声合成で読み上げます。デフォルトの読み上げエンジンはVOICEVOXを使ってます。Voiceroidや棒読みちゃんなどに切り替えることは可能です。

## インストール
### 下準備
1. Javaをインストール
### インストール
#### ウインドーズ
1. ボットのために任意のところにフォルダーを作ってください（権限いらないところで）
2. フォルダーの中でShift＋右クリでPowershellを開く
	3. できない場合はフォルダーパスをコピーして、スタートメニューからPowershellを開く、次のコマンドでフォルダーへ移動します ```cd C:\path\to\your\folder```
4. 以下のコマンドを実行して、必要なファイルをダウンロードします
```
# ボットサーバーのファイルをダウンロードして、解凍します
Invoke-WebRequest -Uri https://github.com/yakisalmon/yomichan/releases/download/beta/yomichan.zip -OutFile yomichan.zip

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

## クレジット
### [VOICEVOX](https://voicevox.hiroshiba.jp/)
VOICEVOX:四国めたん
VOICEVOX:ずんだもん
VOICEVOX:春日部つむぎ
VOICEVOX:波音リツ
