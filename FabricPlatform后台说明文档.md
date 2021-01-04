# FabricPlatform说明文档

## 一.实体类及对应数据库说明

通用类：

1.one.hust.edu.cn.entities.CommonResult<T>

```java
public class CommonResult<T> {//返回的前端的通用的json实体
    private Integer code;//返回状态码
    private String message;//返回消息
    private T data;//返回数据
    public CommonResult(Integer code,String message){
        this(code,message,null);
    }
}
```

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `fabric_user_id` varchar(50) NOT NULL COMMENT 'fabric 用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8 COMMENT='用户信息表';



2.one.hust.edu.cn.entities.User

```java
public class User implements Serializable {//用户实体类
    private Integer id;//用户id
    private String username;//用户名
    private String password;//密码
    private Integer fabricUserId;//fabric中的id
}
```



3.one.hust.edu.cn.entities.MyFile

```java
public class MyFile implements Serializable {//文件实体类
    private Integer id;//文件id
    private String data;//文件数据
    private String dataName;//文件路径
    private String dataType;//文件类型
    private Double dataSize;//文件大小
    private Integer originUserId;//上传人id
    private Timestamp createdTime;//创建时间
    private Timestamp modifiedTime;//修改时间
}
```

CREATE TABLE `data_sample` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `data` text COMMENT '模拟数据',
  `data_name` varchar(255) DEFAULT NULL COMMENT '数据名称',
  `data_type` varchar(255) DEFAULT NULL COMMENT '数据类型',
  `data_size` double DEFAULT NULL COMMENT '数据大小',
  `origin_user_id` int(11) DEFAULT NULL COMMENT '所属人id，对应user_id',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8 COMMENT='模拟数据表';



## 二.用户接口

接口路径：localhost:8080/login

接口参数：one.hust.edu.cn.entities.User

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：用户登录接口，需要用户实体类的username和password，用户进行登录成功后，会产生一个token，有效时间为6000000毫秒。返回CommonResult数据为JSON数据，user对应登录成功的用户信息，token对应该用户产生的token值，该用户在进行后续操作时都应该携带该token，以此验证身份。

接口代码：

```java
//登录
@PostMapping(value = "login")
@LoginToken
public CommonResult login(@RequestBody User user){
    JSONObject jsonObject = new JSONObject();
    boolean result = userService.login(user);
    if(result) {
        User uresult = userService.findUserByUsername(user.getUsername());
        String token = JwtUtil.createJWT(6000000, uresult);
        jsonObject.put("user", uresult);
        jsonObject.put("token", token);
        return new CommonResult<>(200,"登录成功",jsonObject);
    }
    else {
        return new CommonResult<>(400,"登录失败,用户不存在或用户名或密码错误",null);
    }
}
```



接口路径：localhost:8080/register

接口参数：one.hust.edu.cn.entities.User

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：用户注册接口，需要用户实体类的username和password，用户进行注册成功后，会产生一个token，有效时间为6000000毫秒。返回CommonResult数据为JSON数据，user对应登录成功的用户信息，token对应该用户产生的token值，该用户在进行后续操作时都应该携带该token，以此验证身份。

接口代码：

```java
@PostMapping(value = "register")
@LoginToken
public CommonResult register(@RequestBody User user){
        JSONObject jsonObject = new JSONObject();
        if(userService.findUserByUsername(user.getUsername())!=null) return new CommonResult<>(400,"注册失败,用户名已存在",null);
        boolean result = userService.register(user);
        if(result) {
            String token = JwtUtil.createJWT(600000000, user);
            jsonObject.put("token", token);
            jsonObject.put("user", user);
            return new CommonResult<>(200,"注册成功",jsonObject);
        }
        else return new CommonResult<>(400,"注册失败,请联系系统管理员",null);
}
```



## 三.文件接口

接口路径：localhost:8080/uploadFile

接口参数：MultipartFile file和HttpServletRequest httpServletRequest

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：上传文件接口，前端需要传输MultipartFile类型，参数名为file的文件，并且在头部带上该用户的token。前端传输的文件将保存至本机的**D:/研究生资料/南六218实验室/代炜琦项目组文件/github同步代码/uploadFilePackage/**文件夹下，并且保存在数据库中，返回CommonResult数据为MyFile实体类。

接口代码：

```java
//上传文件
@CheckToken
@PostMapping(value = "/uploadFile")
@ResponseBody
public CommonResult uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest){
    //获取文件名
    String fileName = file.getOriginalFilename();
    String filePath = "D:/研究生资料/南六218实验室/代炜琦项目组文件/github同步代码/uploadFilePackage/";
    try {
        //进行文件传输
        file.transferTo(new File(filePath+fileName));
        MyFile myFile = new MyFile();
        myFile.setDataName(filePath+fileName);
        //文件大小以KB作为单位
        // 首先先将.getSize()获取的Long转为String 然后将String转为Float并除以1024 （因为1KB=1024B）
        Double size = Double.parseDouble(String.valueOf(file.getSize())) / 1024;
        BigDecimal b = new BigDecimal(size);
        // 2表示2位 ROUND_HALF_UP表明四舍五入，
        size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // 此时size就是保留两位小数的浮点数
        myFile.setDataSize(size);
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer originUserId = JWT.decode(token).getClaim("id").asInt();
        myFile.setData(TxtUtil.getTxtContent(myFile));
        myFile.setOriginUserId(originUserId);
        myFile.setDataType(fileName.substring(fileName.lastIndexOf("."))+"文件");
        //初次创建时将初始时间和修改时间写成一样
        myFile.setCreatedTime(new Timestamp(new Date().getTime()));
        myFile.setModifiedTime(new Timestamp(new Date().getTime()));
        fileService.uploadFile(myFile);
        return new CommonResult<>(200,"上传成功，文件位于："+filePath+fileName,myFile);
    } catch (Exception e) {
        return new CommonResult<>(400,e.getMessage(),null);
    }
}
```



接口路径：localhost:8080/getDataList

接口参数：无

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：获取所有文件接口，返回MyFile的List集合。

接口代码：

```java
//获取文件列表
@GetMapping(value = "/getDataList")
public CommonResult getDataList(){
    List<MyFile> list = fileService.getDataList();
    return new CommonResult<>(200,"获取所有文件列表成功",list);
}
```



接口路径：localhost:8080/deleteDataById

接口参数：Map<String, String> params和HttpServletRequest httpServletRequest

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：根据id删除文件接口，此接口需要由登录token，并且在头部从httpServletRequest中获取token，params参数中应该传dataId参数，代表待删除的文件id，成功删除返回200状态码。

接口代码：

```java
//根据文件id删除文件
//上传文件
@CheckToken
@PostMapping(value = "/deleteDataById")
@ResponseBody
public CommonResult deleteDataById(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest){
    Integer dataId = Integer.valueOf(params.get("dataId"));
    Integer result = fileService.deleteDataById(dataId);
    // 从 http 请求头中取出 token
    String token = httpServletRequest.getHeader("token");
    if(result<1){
        return new CommonResult<>(400,"不存在id为："+dataId+"的文件",null);
    }
    return new CommonResult<>(200,"成功删除id为："+dataId+"的文件,token为："+token,null);
}
```



接口路径：localhost:8080/getData

接口参数：Map<String, String> params和HttpServletRequest httpServletRequest

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：根据id查看文件接口，此接口需要由登录token，并且在头部从httpServletRequest中获取token，params参数中应该传dataId参数，代表待查看的文件id。如果文件存在，以String类型返回文件内容，作为CommonResult中的数据返回。

接口代码：

```java
//根据文件id获取文件内容
@CheckToken
@PostMapping(value = "/getData")
@ResponseBody
public CommonResult getData(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest){
    Integer dataId = Integer.valueOf(params.get("dataId"));
    MyFile result = fileService.findDataById(dataId);
    // 从 http 请求头中取出 token
    String token = httpServletRequest.getHeader("token");
    if(result==null){
        return new CommonResult<>(400,"不存在id为："+dataId+"的文件",null);
    }

    String txtContent = TxtUtil.getTxtContent(result);
    return new CommonResult<>(200,"文件token为："+token,txtContent);
}
```



接口路径：localhost:8080/updateData

接口参数：Map<String, String> params和HttpServletRequest httpServletRequest

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：根据id查看文件接口，此接口需要由登录token，并且在头部从httpServletRequest中获取token，params参数中应该传dataId参数，代表待修改的文件id，还应该传String类型的dataContent参数，代表修改后的文件内容。修改文件过程为：首先根据文件id查找文件，获取查找到的文件路径，并且删除本地存储的该文件，创建一个新的文件，并把dataContent作为文件内容写入新文件；再更新数据库，更新数据库中data_sample表对应该文件的data为dataContent，data_size为修改后文件的大小和modified_time为修改的时间。

接口代码：

```java
//根据文件id对文件内容进行更新
@CheckToken
@PostMapping(value = "/updateData")
@ResponseBody
public CommonResult updateData(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest){
    Integer dataId = Integer.valueOf(params.get("dataId"));
    String dataContent = params.get("dataContent");
    MyFile myFile = fileService.findDataById(dataId);
    if(myFile==null){
        return new CommonResult<>(400,"不存在id为："+dataId+"的文件",null);
    }
    File old_file = new File(myFile.getDataName());
    old_file.delete();
    File new_file = new File(myFile.getDataName());
    //创建新文件
    try {
        /* 写入Txt文件 */
        new_file.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(new_file));
        out.write(dataContent); // \r\n即为换行
        out.flush(); // 把缓存区内容压入文件
        out.close(); // 最后记得关闭文件
    } catch (Exception e) {
        e.printStackTrace();
    }
    //更新数据库
    myFile.setData(dataContent);
    Double size = Double.parseDouble(String.valueOf(new_file.length())) / 1024;
    BigDecimal b = new BigDecimal(size);
    size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    myFile.setDataSize(size);
    myFile.setModifiedTime(new Timestamp(new Date().getTime()));
    fileService.updateFile(myFile);
    return new CommonResult<>(200,"id为："+dataId+"的文件更新成功",null);
}
```