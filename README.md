# nursingcenter
颐养中心后端 - 单体应用版本。该项目的微服务版本地址如下：
https://github.com/XhKCS/ms-nursingcenter.git

### 更新
#### WebSocket
前端页面可以通过WebSocket来与后端建立一个持久连接，接收后端主动发过来的消息；

创建Websocket连接的路径： `ws://localhost:9000/webSocket/{userId}`

后端发送消息格式：`{类名}_UPDATE`

例如，如果护理级别页面收到了 `NursingLevel_UPDATE` 消息，该页面前端就应该自己向后端发送请求，来更新护理级别的数据

## 前端请求方法
请求端口统一为`9000`（8080端口容易被其他进程占用）；

请求url统一采用**驼峰命名法**，如 http://localhost:9000/nursingLevel/pageByStatus；

请求方式全部为**post**，参数均为json形式；

**返回单个对象数据的请求**方法以`/get`开头，如：`/bed/getByNumber`；

**添加数据的请求**方法以`/add`开头，如：`/bedUsageRecord/add`：

**删除数据的请求**方法以`/delete`开头， 如：`/bedUsageRecord/deleteById`；

**修改数据的请求**方法以`/update`开头，如：`/bedUsageRecord/update`；

**普通list型数据请求**方法以`/list`开头，如：`/bed/listSpareByRoomNumber`；

**分页型数据请求**方法以`/page`开头，如：`/nursingProgram/pageByStatus`；
而且要在对应`list`型请求的参数基础上添加以下两个`number`类型的参数：
* `current`：分页表格当前在第几页
* `size`：分页表格当前一页最多多少条数据


每个请求方法具体需要哪些参数，请在相应的Controller中对应方法内部查看；

## Swagger
Swagger文档及测试地址：http://localhost:9000/swagger-ui/index.html

或 http://localhost:9000/doc.html

## 后端数据统一返回格式
### 普通数据：以`ResponseBean`的格式返回
**请求成功时**返回的数据结构：

```
{
  "status": 200,
  "msg": "success",
  "data": 所需的业务数据
}
```
**请求失败时**（包括没有符合条件的数据等情况）返回的数据结构：
```
{
  "status": 500,
  "msg": 具体错误信息,
  "data": null
}
```

### 分页型数据：以`PageResponseBean`的格式返回
注意请求参数包括`current` 和 `size`；

**请求成功时**返回的数据结构：

```
{
  "status": 200,
  "msg": "success",
  "data": 所需的业务数据（数组，包含的对象个数小于等于`size`）,
  "total": 符合条件的数据总数量
}
```
**请求失败时**（包括没有符合条件的数据等情况）返回的数据结构：
```
{
  "status": 500,
  "msg": 具体错误信息,
  "data": null,
  "total": 0
}
```

### 实体类命名规范（前端应遵循相同的命名规范）
- 床位：`Bed`
- 床位使用记录（详情）：`BedUsageRecord`
- 退住登记：`CheckoutRegistration`
- 客户（老人）：`Customer`
- 客户购买的护理项目记录：`CustomerNursingService`
- 膳食配置项：`DietConfigItem`
- 食品：`Food`
- 护理级别-护理项目（多对多关系类）：`LevelWithProgram`
- 膳食安排项：`MealItem`
- 膳食预订记录：`MealReservation`
- 护理级别：`NursingLevel`
- 护理项目：`NursingProgram`
- 护理记录：`NursingRecord`
- 外出登记：`OutingRegistration`
- 房间：`Room`
- 用户（包括管理员与护工）：`User`

此外，所有类的属性命名均采用驼峰命名法；