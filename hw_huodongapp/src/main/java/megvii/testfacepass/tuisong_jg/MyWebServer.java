package megvii.testfacepass.tuisong_jg;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;
import io.objectbox.Box;

import io.objectbox.query.QueryBuilder;
import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassAddFaceResult;


import megvii.testfacepass.beans.DaKaBean;
import megvii.testfacepass.beans.DaKaBean_;
import megvii.testfacepass.beans.MSRBean;
import megvii.testfacepass.beans.MSRBean_;
import megvii.testfacepass.beans.ResBean;
import megvii.testfacepass.beans.Subject;
import megvii.testfacepass.beans.Subject_;
import megvii.testfacepass.beans.UserInfo;
import megvii.testfacepass.utils.BitmapUtil;


public class MyWebServer extends NanoHTTPD  {

    private final static int PORT = 8092;
   // private Context _mainContext;
    private Box<Subject> subjectBox;
    private FacePassHandler facePassHandler=null;
    private Box<MSRBean> msrBeanBox=null;
    private Box<DaKaBean> daKaBeanBox=null;
    /*
    主构造函数，也用来启动http服务
    */
    public MyWebServer(Box<Subject> subjectBox,Box<MSRBean> msrBeanBox,Box<DaKaBean> daKaBeanBox) throws IOException {
        super(PORT);
       // _mainContext = context;
        this.subjectBox=subjectBox;
        this.msrBeanBox=msrBeanBox;
        this.daKaBeanBox=daKaBeanBox;
        start();
      //  System.out.println("\nRunning! Point your browsers to [http://0.0.0.0:33445/](http://localhost:33445/)\n");
    }

    public void setFacePassHandler(FacePassHandler facePassHandler){
        this.facePassHandler=facePassHandler;
    }

    /*
    解析的主入口函数，所有请求从这里进，也从这里出
    */
    @Override
    public Response serve(IHTTPSession session) {
       // Map<String, String> headers = session.getHeaders();

        Map<String, List<String>> parms = session.getParameters();
        Method method = session.getMethod();
        String uri = session.getUri();
        Map<String, String> files = new HashMap<>();

        if (Method.POST.equals(method) || Method.PUT.equals(method)) {
            try {
                session.parseBody(files);
            } catch (IOException ioe) {
                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,ioe.getMessage()+""));
            } catch (ResponseException re) {
                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,re.getMessage()+""));
            }
        }

        //新增接口
        if ("/app/addFace".equalsIgnoreCase(uri)) {

            try {
                String id = parms.get("id")==null? "" : parms.get("id").get(0);
                String filename = parms.get("faceImage")==null? "" : parms.get("faceImage").get(0);

                String name = parms.get("name")==null? "" : parms.get("name").get(0);
                 name = new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                String b64 = parms.get("b64")==null? "" : parms.get("b64").get(0);
                Log.d("MyWebServer", name);
                String department = parms.get("department")==null? "" : parms.get("department").get(0);//部门
                department = new String(department.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                String tmpFilePath = files.get("faceImage");
                Subject sub = subjectBox.query().equal(Subject_.sid, id).build().findUnique();
                if (sub!=null){
                    //已存在
                    return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"用户已存在"));
                }

                if (null != filename && null != tmpFilePath) {

                    Bitmap bitmap = BitmapFactory.decodeFile(tmpFilePath);
                    if (facePassHandler == null) {
                        return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "SDK没有初始化完成"));
                    }
                    FacePassAddFaceResult result = facePassHandler.addFace(bitmap);
                    if (result.result == 0) {
                        byte[] bytes = result.faceToken;
                        boolean re = facePassHandler.bindGroup("facepasstestx", bytes);
                        if (re) {
                            Subject subject = new Subject();
                            subject.setTeZhengMa(new String(bytes));
                            subject.setId(System.currentTimeMillis());
                            subject.setSid(id+"");
                            subject.setPeopleType("员工");
                            subject.setName(name);
                            subject.setDepartmentName(department);
                            subjectBox.put(subject);
                            return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(0, "入库成功"));
                        } else {
                            return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "入库失败，请重试!(入库时请不要让机器处于识别任务中！)"));
                        }
                    } else {
                        return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "图片质量不合格"));
                    }

                }else if (!b64.equals("")){
                    Bitmap bitmap = base64ToBitmap(b64);
                    if (facePassHandler == null) {
                        return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "SDK没有初始化完成"));
                    }

                    FacePassAddFaceResult result = facePassHandler.addFace(bitmap);
                    if (result.result == 0) {
                        byte[] bytes = result.faceToken;
                        boolean re = facePassHandler.bindGroup("facepasstestx", bytes);
                        if (re) {
                            Subject subject = new Subject();
                            subject.setTeZhengMa(new String(bytes));
                            subject.setId(System.currentTimeMillis());
                            subject.setSid(id+"");
                            subject.setPeopleType("员工");
                            subject.setName(name);
                            subject.setDepartmentName(department);
                            subjectBox.put(subject);
                            return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(0, "入库成功"));
                        } else {
                            return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "入库失败，请重试!(入库时请不要让机器处于识别任务中！)"));
                        }
                    } else {
                        return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "图片质量不合格"));
                    }
                }else {
                    return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "没有图片,无法录入"));
                }

            }catch (Exception e){
                Log.d("MyWebServer", "e:" + e);
                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,e.getMessage()+""));
            }
        }

        //修改接口
        if ("/app/updateFace".equalsIgnoreCase(uri)) {

            try {
                String id = parms.get("id") == null ? "" : parms.get("id").get(0);
                String filename = parms.get("faceImage") == null ? "" : parms.get("faceImage").get(0);
                String name = parms.get("name") == null ? "" : parms.get("name").get(0);
                name = new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                String b64 = parms.get("b64") == null ? "" : parms.get("b64").get(0);
                Log.d("MyWebServer", name+"");
                String department = parms.get("department") == null ? "" : parms.get("department").get(0);//部门
                department = new String(department.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                String tmpFilePath = files.get("faceImage");
                Subject sub = subjectBox.query().equal(Subject_.sid, id).build().findUnique();
                if (sub == null) {
                    return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"要修改的人员不存在"));
                }else {

                    if ((null == filename || null == tmpFilePath) && b64.equals("")) {
                        //两个文件都是空
                        sub.setName(name);
                        sub.setDepartmentName(department);
                        subjectBox.put(sub);
                        return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(0,"修改成功"));

                      }else if (null != filename && null != tmpFilePath){
                        //文件存在
                        Bitmap bitmap = BitmapFactory.decodeFile(tmpFilePath);
                        if (facePassHandler == null) {
                            return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "SDK没有初始化完成"));
                        }

                        FacePassAddFaceResult result = facePassHandler.addFace(bitmap);
                        if (result.result == 0) {

                            byte[] bytes = result.faceToken;
                            boolean re = facePassHandler.bindGroup("facepasstestx", bytes);
                            if (re) {
                                facePassHandler.deleteFace(sub.getTeZhengMa().getBytes());
                                sub.setTeZhengMa(new String(bytes));
                                sub.setName(name);
                                sub.setDepartmentName(department);
                                subjectBox.put(sub);
                                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(0, "入库成功"));
                            } else {
                                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "入库失败，请重试!(入库时请不要让机器处于识别任务中！)"));
                            }
                        } else {
                            return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "图片质量不合格"));
                        }
                    }else {
                       Bitmap bitmap =base64ToBitmap(b64);
                        if (facePassHandler == null) {
                            return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "SDK没有初始化完成"));
                        }

                        FacePassAddFaceResult result = facePassHandler.addFace(bitmap);
                        if (result.result == 0) {

                            byte[] bytes = result.faceToken;
                            boolean re = facePassHandler.bindGroup("facepasstestx", bytes);
                            if (re) {
                                facePassHandler.deleteFace(sub.getTeZhengMa().getBytes());
                                sub.setTeZhengMa(new String(bytes));
                                sub.setName(name);
                                sub.setDepartmentName(department);
                                subjectBox.put(sub);
                                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(0, "入库成功"));
                            } else {
                                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "入库失败，请重试!(入库时请不要让机器处于识别任务中！)"));
                            }
                        } else {
                            return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "图片质量不合格"));
                        }

                    }

                }

            }catch (Exception e){
                Log.d("MyWebServer", "e:" + e);
                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,e.getMessage()+""));
            }
        }

        //查询单个人员
        if ("/app/searchUserInfo".equalsIgnoreCase(uri)) {
            try {
            if (facePassHandler == null) {
                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "SDK没有初始化完成"));
            }
                String id = parms.get("id") == null ? "" : parms.get("id").get(0);

                if (id.equals("")){
                    return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"参数错误"));
                }else {
                    Subject sub = subjectBox.query().equal(Subject_.sid, id).build().findUnique();
                    if (sub!=null){
                        try {

                            Bitmap bitmap=facePassHandler.getFaceImage(sub.getTeZhengMa().getBytes());

                            UserInfo userInfo=new UserInfo();
                            userInfo.setId(sub.getSid());
                            userInfo.setDepartment(sub.getDepartmentName());
                            userInfo.setName(sub.getName());
                            userInfo.setB64(BitmapUtil.bitmaptoString(bitmap));

                            JsonObject jsonObject=new JsonObject();
                            Gson gson =new Gson();
                            jsonObject.addProperty("code",0);
                            jsonObject.add("userInfo",gson.toJsonTree(userInfo));
                            return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",jsonObject.toString());

                        } catch (Exception e) {
                           // e.printStackTrace();
                            return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"获取本地b64失败"));
                        }

                    }else {
                        return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"未查询到该人员信息"));

                    }
                 }
                }catch (Exception e){
                 return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,e.getMessage()+""));

            }

        }


         //删除接口
        if ("/app/deleteFace".equalsIgnoreCase(uri)) {
            if (facePassHandler==null){
                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"SDK未初始化好"));
            }else {

                try {
                    String id = parms.get("id")==null? "" : parms.get("id").get(0);
                    Subject subject = subjectBox.query().equal(Subject_.sid, id).build().findUnique();
                    if (subject!=null){
                        facePassHandler.deleteFace(subject.getTeZhengMa().getBytes());
                        subjectBox.remove(subject);
                        return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(0,"删除成功!"));
                    }else {
                        return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"未找到该本地人员信息!"));
                    }
                } catch (Exception e) {
                    return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"发生错误"+e.getMessage()));
                }
            }
        }


        //查询接
        if ("/app/getFaceRecord".equalsIgnoreCase(uri)) {
            try {
            long page = Long.valueOf(parms.get("page")==null? "" : parms.get("page").get(0));
            long count = Long.valueOf(parms.get("count")==null? "" : parms.get("count").get(0));
            int state = Integer.valueOf(parms.get("state")==null? "0" : parms.get("state").get(0));//0入库人员 1陌生人
            long startTime =  Long.valueOf(parms.get("startTime")==null? "" : parms.get("startTime").get(0));
            long endTime =  Long.valueOf(parms.get("endTime")==null? "" : parms.get("endTime").get(0));
            Gson gson =new Gson();
            if (state==0){
                //0入库人员
                if (count>10){
                    count=10;
                }
               // Log.d("MyWebServer", "daKaBeanBox.getAll().size():" + daKaBeanBox.getAll().size());
             QueryBuilder<DaKaBean> builderDaKaBean=daKaBeanBox.query();
             builderDaKaBean.greater(DaKaBean_.time, startTime) //大于开始时间
                               .and().less(DaKaBean_.time,endTime); //小于结束时间
             List<DaKaBean> daKaBeanList=builderDaKaBean.build().find((page-1)*count,count);
                Log.d("MyWebServer", "daKaBeanList.size():" + daKaBeanList.size());
             if (daKaBeanList.size()>0){
                 JsonObject jsonObject=new JsonObject();
                 JsonArray array=new JsonArray();
                 String paths= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"ruitongzipmbj";
                 for (DaKaBean daKaBean :daKaBeanList){
                     try {
                         String ss= BitmapUtil.bitmaptoString(BitmapFactory.decodeFile(
                                 paths+File.separator+daKaBean.getTime()+".png"));
                         daKaBean.setB64(ss);
                     }catch (Exception e){
                         e.printStackTrace();
                         daKaBean.setB64("转换为b64时发生错误");
                     }
                     array.add(gson.toJsonTree(daKaBean));
                 }
                 jsonObject.addProperty("code",0);
                 jsonObject.add("array",array);
                 return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",jsonObject.toString());
             }else {
                 return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"没有查询到数据"));
             }

            }else {
                //查询陌生人
                if (count>10){
                    count=10;
                }

                QueryBuilder<MSRBean> builderMSR=msrBeanBox.query();
                builderMSR.greater(MSRBean_.time2, startTime)
                        .and().less(MSRBean_.time2,endTime);
                List<MSRBean> daKaBeanList=builderMSR.build().find((page-1)*count,count);
                if (daKaBeanList.size()>0){
                    JsonObject jsonObject=new JsonObject();
                    JsonArray array=new JsonArray();
                    for (MSRBean daKaBean :daKaBeanList){
                        array.add(gson.toJsonTree(daKaBean));
                    }
                    jsonObject.addProperty("code",0);
                    jsonObject.add("array",array);
                    return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",jsonObject.toString());
                }else {
                    return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"没有查询到数据"));
                }
            }
            }catch (Exception e){
                return  newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,e.getMessage()+"")) ;
            }
        }
        //删除所有
        if ("/app/deleteAll".equalsIgnoreCase(uri)) {
            if (facePassHandler == null) {
                return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "SDK未初始化好"));
            } else {
                try {
                    List<Subject> subjectList = subjectBox.getAll();
                    if (subjectList==null || subjectList.size()==0){
                        return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, "没有人员可供删除"));
                    }
                    for (Subject subject : subjectList) {
                        try {
                            facePassHandler.deleteFace(subject.getTeZhengMa().getBytes());
                        } catch (FacePassException e) {
                            e.printStackTrace();
                        }
                        subjectBox.remove(subject);
                    }
                    return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(0, "已删除全部人员"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1, e.getMessage() + ""));
                }
            }
        }
        //最后的
        return  newFixedLengthResponse(Response.Status.OK,"application/json;charset=UTF-8",getResponse(-1,"未找到对应方法")) ; // =========》  返回给客户端
    }


    private String getResponse(int code,String message){
        Gson gson =new Gson();
        ResBean resBean=new ResBean();
        resBean.setCode(code);
        resBean.setMessage(message);
        return gson.toJson(resBean);
    }

//    private String utf8(String ss){
//
//        try {
//            return  new String( ss.getBytes("ISO8859-1") , "GBK");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    /**
     * base64转为bitmap
     * @return
     */
    private  Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}