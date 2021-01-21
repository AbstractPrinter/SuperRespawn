# SuperReSpawn

一款功能强大的重生点管理插件。

> 下方表格打×的为待实现

## 功能

| 序号 | 功能 | 是否实现 |
| :----: | :---- | :----: |
| 1 | 每个世界都允许有多个重生点 | √ |
| 2 | 按照距离选择重生点 | √ |
| 3 | 按照权重选择重生点 | √ |
| 4 | 检测是否有复活在某个重生点的权限 | √ |
| 5 | 重生点特效 | × |
| 6 | 重生后向玩家发送自定义 Title/SubTitle | √ |
| 7 | 重生后向玩家发送自定义 Chat | √ |

## 指令

| 指令 | 功能 | 权限节点 |
| :---- | :---- | :---- |
| /srspawn help | 显示帮助 | superrespawn.command.help |
| /srspawn mode <default,nearest,weight> | 设置重生模式 | superrespawn.command.mode |
| /srspawn add <spawn_name> | 以自身为坐标,添加一个重生点 | superrespawn.command.add |
| /srspawn del <spawn_name> | 删除一个重生点 | superrespawn.command.del |
| /srspawn set <spawn_name> | 对一个重生进行设置 | superrespawn.command.set |
| /srspawn list | 列出所有重生点 | superrespawn.command.list |
| /srspawn reload | 重载插件 | superrespawn.command.reload |

## 权限

| 权限节点 | 功能介绍 |
| :---- | :---- |
| superrespawn.command.指令名 | 指令的权限节点 |
| superrespawn.spawn.重生点名 | 决定玩家是否有权限复活在某点 |

## Future
* 实现重生特效,每一个玩家都可以自行设置自己的重生特效,每一个特效都需要相应权限
* 重生空隙时间+重生冷却
* 付费重生,或者扣除相应物品
* 重生前观察者视角

## 建议&问题提交
https://github.com/AbstractPrinter/SuperRespawn/issues

## 开源地址
https://github.com/AbstractPrinter/SuperRespawn

## Jenkins 下载地址
https://jenkins.royun.co/job/SuperRespawn