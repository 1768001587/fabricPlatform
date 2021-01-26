# fabricPlatform
基于fabric的测试平台
## Swagger
### 注解说明
- @Api：用于修饰Controller类，生成Controller相关文档信息
- @ApiOperation：用于修饰Controller类中的方法，生成接口方法相关文档信息
- @ApiParam：用于修饰接口中的参数，生成接口参数相关文档信息
- @ApiModelProperty：用于修饰实体类的属性，当实体类是请求参数或返回结果时，直接生成相关文档信息





## 项目后台启动说明

**由于网络环境，以下启动顺序配置在私人电脑上：**

本机cmd进入D:\软件\mongodb\bin，输入**mongod --dbpath D:\软件\mongodb\database\data**命令启动mongodb；

本机进入D:\软件\natapp，双击打开**natapp.exe**获取nat转换后的地址。如果出现问题，进入https://natapp.cn/tunnel/edit/dpd9wm8967进行查看配置；

打开navicat和robo 3T 1.3.1可视化软件分别查看mysql数据库和mongo数据库



