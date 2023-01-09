## monsiaj-mac ##
monsiaj version 2.0.31 を元にカスタマイズした。Java 17 で運用中。

### pom.xml ###
jlink, jpackage を使って、mac アプリを生成するようにした。

### org.montsuqi.mosiaj.client.widgethandlers.PandaTableHandler ###
(K02)診療行為入力画面で編集を行ってリターンキーを押した後，セルの選択が残るようにした。

### org.montsuqi.monsiaj.widgets.PandaTable ###
(K02)診療行為入力画面の PandaTable の空白行を無視して，データがある行だけを表示するようにした。

### org.montsuqi.monsiaj.widgets.PandaCList ###
システムプロパティー monsia.wiget.pandaclist.showgrid がセットされていた場合，テーブルに grid を表示する。

### org.montsuqi.monsiaj.client.UIControl ###
システムプロパティー monsia.errordialog.alwaysontop が設定されていた場合、(P031)患者登録-オンライン認証画面で、エラーダイアログなどのモーダルなダイアログを常に最前面に表示する。モーダルなダイアログが後ろに回って操作不能になるのを防ぐ。

### org.montsuqi.monsiaj.widgets.PandaFocusManager ###
* (K03)請求確認 で ctrl-0 で「領収書，明細書，処方発行無し」，ctrl-1 で「領収書，明細書だけ発行あり」，ctrl-2 で「処方だけ発行あり」，ctrl-3 で「全て発行あり」に切り替える。
* (XC01)プレビュー選択画面で前行，次行，前頁，次頁のショートカットに上下左右キーを追加。
* (K02)診療行為入力画面で、ctrl-ENTER で中途表示データ展開、ctrl-K で外来管理加算削除。
* (C02)病名登録画面で、ctrl-K で疾患区分クリア。

### [備忘録] monsiaj で設定できるその他のプロパティ ###
* monsia.cert_expire_check_monthes --- クライアント証明書の有効期限を予告する月数
* monsia.config.reset_user --- ログインユーザー名をリセットする
* monsia.debug.jsonrpc --- JSON-RPC を表示する
* monsia.debug.printer_list --- 未実装
* monsia.debug.widget.border --- widget に Etched Border を付ける
* monsia.dialog.position --- topleft/topright/bottomleft/bottomright
* monsia.disable.panda_html --- 業務メニューにお知らせを表示しない
* monsia.disable_push_client --- pusher を使用しない
* monsia.do_profile --- JSON-RPC の情報を表示する
* monsia.document.handler --- Glade1Handler/MonsiaHandler を指定する
* monsia.log.level --- ログレベルを設定する
* monsia.pandaclist.rowheight --- PandaCList の行高
* monsia.pandaclist.selection_bg_color --- PandaCList の背景色
* monsia.pandaclist.selection_fg_color --- PandaCList の前景色
* monsia.pandatable.rowheight --- PandaTable の行高
* monsia.ping_timer_period --- ping を送る間隔
* monsia.popup_notify_position --- topleft/leftup/bottomleft/leftbottom/bottomright/rightbottom
* monsia.printreport.showdialog --- ダウンロードしたファイルのレポートを表示する
* monsia.pusher_uri --- pusher の uri を指定する
* monsia.save.print_data --- 未実装
* monsia.topwindow.height --- トップウインドウの高さを指定する
* monsia.topwindow.width --- トップウインドウの幅を指定する
* monsia.topwindow.x --- トップウインドウの位置を指定する
* monsia.topwindow.y --- トップウインドウの位置を指定する
* monsia.update_cert_api_host --- certificate update ホストを指定する
* monsia.use.loader --- Loader を使うかどうか
* monsia.user.font --- フォントを指定する
* monsia.widgets.window.insets_ratio --- ダイアログを表示する領域の割合
