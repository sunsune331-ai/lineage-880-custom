# J.J. article-level reread — R41–R50

> Final checkpoint of the requested reread. Formal completed baseline remains **88 / 783; remaining 695**. No reread increments completion.

### R41 — 天堂私服 | 主程式安裝及登入器設定與登入
- **Canonical/original URL:** https://morosedog.gitlab.io/private-lineage-20210723-private-lineage-11/
- **Source/date:** J.J.'s Blogs / J.J. Huang / 2021-07-23
- **Classification:** Shared
- **Versions/access:** Lineage 3.81C client + L1J-3.80c server + Login_v380a / PUBLIC_FULL
- **Main content:** complete old setup bridge from running server, installing client and configuring launcher endpoint through login and character creation.
- **Environment/tools:** VirtualBox Win7 x64; Lineage 3.81C; Login_v380a. Launcher root includes `LoginUpdate`, 登入器, 編碼器, `封包加密核心修改.txt`, `移動封包修改.txt`.
- **Encoder/config:** run `Encode.exe` (`Encode v3.80`); fields example Name `快樂天堂`, IP `127.0.0.1` for local demo, Port `2000`, Version default; encoding generates new `Login.ini`; copy it into 登入器 directory.
- **Import flow:** copy configured launcher directory contents into Lineage 3.81C root, replacing conflicts.
- **Display constraint:** article says old game may require Windows display `全彩 16位元`; otherwise login/window crash/error can occur in that environment.
- **Runtime flow:** launch `Login.exe` -> Start -> green server-status light -> select server -> account/password (article's server setup permits direct account creation) -> character selection/creation -> game.
- **Limit/security notes:** portable clients may include ads or potentially malicious additions; executables are unknown publisher; 16-bit display and port 2000 are historical, not 8.8 requirements.
- **Packet/class:** no numeric opcode; documents separate packet-encryption/movement compatibility files.
- **Cross-support:** corroborates R49/R50 launcher components and R42–48 endpoint/network articles.
- **8.8 relation:** verify actual `LinLogin`/client bootstrap: server-list source, endpoint/version metadata, process handoff, executable launched, connection status probe and account/login path.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R42 — 天堂私服 | IP 位址 (IP Address)
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210724-private-lineage-12/ / 2021-07-24
- **Classification/access:** Other / PUBLIC_FULL
- **Version:** general networking background, motivated by exposing old emulator to remote users.
- **Content:** distinguishes IP protocol from IP address; IPv4 decimal dotted form (`172.16.254.1`) vs IPv6 hex colon form (`2001:db8:...`); IPv4 as 32 bits; examples convert binary to 0.0.0.0 / 255.255.255.255 / 192.168.42.1. Introduces historical Class A–E, E reserved, and Net_ID/Host_ID explanation using 192.168.0.0/24-like example.
- **Files/classes/packet:** none game-specific.
- **Limits:** classful addressing is pedagogical/historical networking framing and should not be used as modern routing design. The article is not evidence of a game transport.
- **Cross-support:** terminology basis for R43–R48.
- **8.8 relation:** only endpoint vocabulary. Runtime must determine actual addresses/transport.
- **Tags:** `待驗證` for project mapping; `僅 research evidence`.

### R43 — 天堂私服 | 內部、外部網路
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210725-private-lineage-13/ / 2021-07-25
- **Classification/access:** Other / PUBLIC_FULL
- **Version:** general networking/legacy deployment context.
- **Technical details:** `localhost` resolves to local loopback (commonly 127.0.0.1 / IPv6 ::1); 127.0.0.0/8 reserved for loopback; 0.0.0.0 described as non-routable meta-address with context-dependent client/server meanings. Discusses LAN/internal vs external network, ISP dial-up obtaining public IP, and DHCP automatic allocation.
- **Deployment cases:** depending topology, launcher advertises external/public IP, while one or more modem/router devices may require Virtual Server / Port Forwarding. Article treats those names as essentially the same NAT-forwarding concept.
- **Explicit gap:** a combined modem + WiFi DHCP topology article was intentionally absent because author lacked that environment.
- **Limits:** simplified/home-network model; DHCP/class-C statements are not universal infrastructure facts.
- **Cross-support:** R45–R48 instantiate the cases; R41/R50 encoder uses advertised endpoint.
- **8.8 relation:** keep `bind address`, `LAN host`, `advertised endpoint`, `public/NAT endpoint` as separate evidence fields.
- **Tags:** `待驗證` `僅 research evidence`.

### R44 — 天堂私服 | 防火牆輸入輸出規則
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210726-private-lineage-14/ / 2021-07-26
- **Classification/access:** Protocol / PUBLIC_FULL
- **Version:** general network teaching + Win7/L1J legacy setup.
- **Content:** firewall, inbound vs outbound, port endpoint, TCP vs UDP overview. Article explicitly advises against disabling firewall as the normal solution, though later suggests temporary disable as a diagnostic test.
- **Legacy setup:** says many old Lineage emulators use port 2000; Win7 Advanced Firewall -> Inbound Rules -> New Rule -> Port -> TCP -> specific local port 2000 -> Allow -> profiles -> name.
- **Diagnostic flow:** if cannot connect, temporary firewall-off test; if still unable, likely not firewall in author's simplified diagnosis; if host uses private IP, external access also needs forwarding.
- **Networking claims/limits:** article includes simplified TCP/UDP comparison and historical port-range descriptions; game use of TCP/UDP must not be inferred from generic tutorial examples. Disabling firewall is not retained as recommended operational practice, only as source-described diagnostic behavior.
- **Cross-support:** R45–48 forwarding; R41 endpoint 2000.
- **8.8 relation:** first inspect actual server listen socket and client connect trace, then define least-privilege firewall rule; do not reuse 2000/TCP without evidence.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R45 — 天堂私服 | 虛擬服務器和通訊埠轉發
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210727-private-lineage-15/ / 2021-07-27
- **Classification/access:** Protocol / PUBLIC_FULL
- **Version:** generic NAT with legacy Lineage example.
- **Main:** Virtual Server / Port Forwarding maps an external address:port to an internal LAN host:port.
- **Generic examples preserved:** `118.163.27.88:8080 -> 192.168.0.111:8080`; `:548 -> 192.168.0.168:548`; `:21 -> 192.168.0.188:21`.
- **Legacy device example:** Chunghwa DSL-6740C (2T2R); WAN Setup index `WAN1_2`; Virtual Server rule enabled; Name `LineageServer2000`; Internal IP `192.168.1.101`; internal/external start/end port 2000; Protocol Type Both (TCP/UDP). Result: public IP:2000 forwarded to internal IP:2000.
- **Limits:** device UI/port/protocol are demonstration values, not project protocol evidence.
- **Cross-support:** instantiated again in R47 and WiFi variant R48.
- **8.8 relation:** distinguish server bind/listen, NAT mapping and public reachability from application packet bugs.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R46 — 天堂私服 | 對外設定(撥接上網)
- **Canonical/original URL:** https://morosedog.gitlab.io/private-lineage-20210728-private-lineage-16/
- **Source/date:** J.J.'s Blogs / 2021-07-28
- **Classification/access:** Protocol / PUBLIC_FULL main body reacquired through site search after direct reader endpoint returned a transient Internal Error.
- **Version:** legacy deployment example.
- **Network layout:** computer directly uses ADSL dial-up and obtains public IP directly.
- **Config example:** encoder IP set to public `61.231.231.231`; launcher setup follows R41; firewall follows R44. No NAT forwarding is needed in this topology as described.
- **Source caveat:** author claims the shown Chunghwa ISP setup differs from some Cable ISP environments and suggests Hamachi as possible workaround. This is historical/provider-specific advice and must not be generalized.
- **Files/classes/packet:** only launcher endpoint config; no opcode.
- **Cross-support:** is topology case #1 from R43 and contrasts with R47/R48 NAT cases.
- **8.8 relation:** advertised endpoint may equal directly assigned public address only when runtime network topology actually does so.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R47 — 天堂私服 | 對外設定(數據機DHCP)
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210729-private-lineage-17/ / 2021-07-29
- **Classification/access:** Protocol / PUBLIC_FULL
- **Version:** legacy home-network example.
- **Topology:** modem dials upstream; modem DHCP assigns private/virtual IP to host; example external public IP `61.231.231.231`.
- **Config:** launcher encoder advertises public IP, not host private address; DSL-6740C WAN Setup `WAN1_2`; Virtual Server enabled; Name LineageServer2000; internal host 192.168.1.101; internal/external ports 2000–2000; Protocol Both. ExternalIP:2000 maps to internalIP:2000.
- **Limits:** specific hardware/provider/IP/port values are historical examples.
- **Cross-support:** near-direct instantiation of R45.
- **8.8 relation:** record DHCP-assigned host address stability and NAT forwarding separately from server/app protocol.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R48 — 天堂私服 | 對外設定(WiFi-DHCP)
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210730-private-lineage-18/ / 2021-07-30
- **Classification/access:** Protocol / PUBLIC_FULL
- **Version:** legacy home-network example.
- **Topology:** clients attach to WiFi router; router attaches to modem/dials upstream; WiFi DHCP assigns private host IP; public example `61.231.231.231`.
- **Config:** encoder advertises public IP; TP-Link AX3000 example -> Advanced -> NAT Forwarding -> Virtual Servers -> Add; Service Type `LineageServer`; external port 2000; internal IP 192.168.1.101; internal port 2000; protocol `全部` (TCP/UDP). PublicIP:2000 forwards to privateIP:2000.
- **Limits:** hardware/UI/address/port are demonstration values; does not prove 8.8 transport.
- **Cross-support:** confirms R43/R45 separation of public endpoint from LAN host.
- **8.8 relation:** runtime network runbook should test local listen -> LAN reachability -> NAT mapping -> external reachability in that order.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R49 — 天堂私服 | 登入器簡介
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210801-private-lineage-20/ / 2021-08-01
- **Classification/version/access:** Client / Login_v380a with old 3.80/3.81 client ecosystem / PUBLIC_FULL
- **Root components:** `LoginUpdate` + `Update.ini`; 登入器 containing `Login.exe`; 編碼器 containing `Encode.exe`; `封包加密核心修改.txt`; `移動封包修改.txt`.
- **Encoder directory:** `Encode.exe`; `spr_action.exe` converts morph/action data to server DB `spr_action.sql`; article says old server uses it to detect abnormal movement/attack speed.
- **Launcher directory:** `Login.exe` reads encoded `Login.ini` to route to target server; `Login.cfg` basic launcher config; `Login.ini` target-server encoded config; `eat.exe/eat.dll` ingest patches including HTML/TBL, SPR, XML, TIL; `LinHelperZ.txt` config for built-in helper; `TW13081901.bin` described as actual game core (`Lin.bin`, with naming variants); `Microsoft.VC90.CRT` runtime dependency for this generation; `skin` launcher images controlled through Login.cfg.
- **XML/patch directory:** `TW13081901.xml`; later 2022-03-26 note says `ReNStatusWinUI.xml` defines equipment/status UI images/text positions/sizes/strings with `string-c.tbl`, and placing it in this launcher-supplied XML directory can cause it to be used at startup without eat. Article explicitly phrases broader priority as an assumption (`可假設`), so it is not upgraded to proven loader precedence.
- **Update examples:** `Update.ini`, `test.zip`, `text.zip` scheduling/examples.
- **Limits/conflicts:** launcher-package contents vary by supported client; runtime-library and resource priority claims are generation-specific; generalized XML priority is explicitly tentative in source.
- **Cross-support:** supports R01 eat, old spr_action research, R50 functions and the existing launcher/morph-precedence articles.
- **8.8 relation:** inventory `LinLogin` modules/config/resources and trace actual open/process launch/override behavior. Especially test any loose XML priority claim rather than accepting it.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R50 — 天堂私服 | 登入器功能說明
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210802-private-lineage-21/ / 2021-08-02
- **Classification/version/access:** Shared / Login_v380a / PUBLIC_FULL
- **Main:** documents three interrelated tabs: Encoding, Tools, Other; launcher is a configuration/update/protocol/resource component, not just a start button in this old ecosystem.
- **Encoding fields:** server Name; IP or domain; Port; Version (Login_v380a says supports 380; default example `TW13081901`). Server-list slots `Server1`–`Server8`. Encoding emits `Login.ini`; sample contains `[Server1]`, `ServerName`, encrypted `ServerData`.
- **Feature toggles:** RSA packet encryption (requires generated key + server code changes from `封包加密核心修改.txt`); basic anti-cheat; advanced anti-cheat; personal morph file (requires morph encoding); built-in `LinHelperZ` helper opened with Home key; `移動封包不加密` requiring server changes from `移動封包修改.txt`; multi-client allowed.
- **Tool tab:** generate key -> `pack.properties`; morph encode `TW13081901.txt -> TW13081901.pak`; Blog encoding further encodes Login.ini server-list content to clipboard, framed by `databegin...dataend`, for web-based list updates.
- **Other tab:** connection status red/green light; announcement webpage; list update fetches configured URL for server list; auto-update uses `LoginUpdate/Update.ini` and hosted files; market-price button opens configured web content; Save writes/modifies `Login.cfg`.
- **Protocol/resource notes:** article states launcher/server packets can use RSA in this optional legacy configuration and movement can be excluded from encryption. No numeric opcode or handshake format is documented here.
- **Limits:** many features say tutorial `待補`; anti-cheat implementation details are not given; `ServerData` is only described as encrypted, algorithm for that field is not established by this page. Feature availability is Login_v380a-specific.
- **Cross-support:** corroborates R41 config, R49 file roles, morph-PAK/source-precedence corpus, spr_action timing corpus, and networking endpoint articles.
- **8.8 relation:** separate validation axes: endpoint/server list; process handoff; crypto handshake/adaptation; resource/morph PAK loading; update/patch URL; status probe; multi-instance. Never assume LinLogin 8.8 retains any individual old feature.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

## Checkpoint 50 status
- **Reread attempted this checkpoint:** 10
- **Full main body reacquired:** 10 (R46 reacquired via the site's indexed body after a transient direct-reader error)
- **Main body inaccessible:** 0
- **Article-level records completed total:** **50 / 50**
- **Articles with material details absent from previous batch summary:** 10
- **New verification items:** launcher file/component inventory; old 16-bit display constraint; loopback/bind/public/NAT distinction; actual listen transport vs generic TCP/UDP tutorial; launcher XML loose-priority hypothesis; server-list `ServerData`; optional RSA/movement exception; morph TXT->PAK; web server-list update; update/patch/status probe ownership.
- **Previous-summary claims not re-confirmed:** 0. Several prior generalizations are now qualified as version-/device-specific.
- **Difference vs previous summary:** yes — substantial omitted article-level detail, especially launcher file graph and network topology parameters.
- **Baseline impact:** **none; remains 88 / 783, remaining 695**.

## Final category counts for the same 50 reread articles
- Client: 17
- Server: 9
- Protocol: 9
- Shared: 11
- Other: 4
- Total: 50
