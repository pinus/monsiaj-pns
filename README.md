## monsiaj-pns ##
monsiaj をカスタマイズしたもの。

## カスタマイズ内容 ##
monsiaj version 2.0.15 を元にカスタマイズした。

### org.montsuqi.mosiaj.client.widgethandlers.PandaTableHandler ###
(K02)診療行為入力画面で編集を行ってリターンキーを押した後，セルの選択が残るようにした。

### org.montsuqi.monsiaj.widgets.PandaCList ###
システムプロパティー monsia.pandaclist.showgrid がセットされていた場合，テーブルに grid を表示する。

### org.montsuqi.monsiaj.widgets.PandaFocusManager ###
K03(請求確認) でショートカット処理を加えた。ctrl-0 で「領収書，明細書，処方発行無し」，ctrl-1 で「領収書，明細書だけ発行あり」，ctrl-2 で「処方だけ発行あり」，ctrl-3 で「全て発行あり」に切り替える。

### org.montsuqi.monsiaj.widgets.PandaTable ###
(K02)診療行為入力画面の PandaTable の空白行を無視して，データがある行だけを表示するようにした。
