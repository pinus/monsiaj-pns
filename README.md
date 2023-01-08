## monsiaj-pns ##
monsiaj をカスタマイズしたもの。Java 1.8 用。

## カスタマイズ内容 ##
monsiaj version 2.0.31 を元にカスタマイズした。

### org.montsuqi.mosiaj.client.widgethandlers.PandaTableHandler ###
(K02)診療行為入力画面で編集を行ってリターンキーを押した後，セルの選択が残るようにした。

### org.montsuqi.monsiaj.widgets.PandaCList ###
システムプロパティー monsia.wiget.pandaclist.showgrid がセットされていた場合，テーブルに grid を表示する。

### org.montsuqi.monsiaj.widgets.PandaFocusManager ###
K03(請求確認) で ctrl-0 で「領収書，明細書，処方発行無し」，ctrl-1 で「領収書，明細書だけ発行あり」，ctrl-2 で「処方だけ発行あり」，ctrl-3 で「全て発行あり」に切り替える。XC01(プレビュー選択画面)で前行，次行，前頁，次頁のショートカットに上下左右キーを追加。

### org.montsuqi.monsiaj.widgets.PandaTable ###
(K02)診療行為入力画面の PandaTable の空白行を無視して，データがある行だけを表示するようにした。

monsia.errordialog.alwaysontop=true

参考 monsiaj のその他のプロパティ

#monsia.logger.level=TRACE
monsia.user.font=SansSerif-plain-12
monsia.pandapreview.initialzoom=125
monsia.widgets.pandaclist.showgrid=true
monsia.pandatable.rowheight=20
monsia.errordialog.alwaysontop=true


monsia.popup_notify_position
monsia.document.handler
monsia.user.font.
monsia.debug.widget.border
monsia.config.reset_user
monsia.ping_timer_period
monsia.debug.jsonrpc
monsia.do_profile
monsia.debug.jsonrpc
monsia.pusher_uri
monsia.disable_push_client
monsia.do_profile
monsia.debug.printer_list
monsia.cert_expire_check_monthes
monsia.update_cert_api_host
monsia.printreport.showdialog
monsia.save.print_data
monsia.log.level
monsia.use.loader
monsia.widgets.window.insets_ratio
monsia.dialog.position

monsia.topwindow.width
monsia.topwindow.width
monsia.topwindow.height
monsia.topwindow.height
monsia.topwindow.x
monsia.topwindow.x
monsia.topwindow.y
monsia.topwindow.y
monsia.pandaclist.rowheight
monsia.pandaclist.selection_bg_color
monsia.pandaclist.selection_fg_color
monsia.widget.pandaclist.showgrid
monsia.pandatable.rowheight
monsia.pandatable.rowheight
monsia.disable.panda_html