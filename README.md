# ThePermissions

```java 
String  permission=Manifest.permission.CAMERA;
String[]permission={Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};

MyPermission.get(this).request(permission, new PermissionCallback() {
    @Override
    public void granted() {
        //权限全部允许
    }
    @Override
    public void denied(String firstDenied) {
        //拒绝单个权限或者全部权限
    }
   //此处忽略两个重写方法(eachGranted和eachDenied)
   //eachGranted:每个被允许的权限
   //eachDenied:每个被拒绝的权限
});

//Manifest里面所有需要动态申请的权限
MyPermission.get(this).requestAll(PermissionCallback);
```  

### 如果本库对您有帮助,还希望支付宝扫一扫下面二维码,你我同时免费获取奖励金(非常感谢 Y(^-^)Y)
![github](https://github.com/zhongruiAndroid/SomeImage/blob/master/image/small_ali.jpg?raw=true "github")  

      
| 最新版本号 | [ ![Download](https://api.bintray.com/packages/zhongrui/mylibrary/MyPermissions/images/download.svg) ](https://bintray.com/zhongrui/mylibrary/MyPermissions/_latestVersion) |
|--------|----|
  
   
```gradle 
implementation 'com.github:MyPermissions:版本号看上面'
```
<br/>
<br/>
<br/>  

Thanks https://github.com/tbruyelle/RxPermissions
