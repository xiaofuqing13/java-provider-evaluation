<template>
  <div>
    <div class="search">
<p>欢迎注册您的店铺！</p>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="showFormDialog" >注册</el-button>

    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="image" label="图片" show-overflow-tooltip>
          <template slot-scope="scope">
            <img :src="require('@/assets/imgs/bg1.jpg')" style="width: 50px; height: 50px;" alt="图片">
          </template>
        </el-table-column>
        <el-table-column prop="name" label="店铺名字" show-overflow-tooltip></el-table-column>
        <el-table-column prop="serviceCount" label="服务详情"></el-table-column>
        <el-table-column prop="address" label="地址"></el-table-column>



        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="showLargeImage(scope.row.image)" size="mini">查看</el-button>

          </template>
        </el-table-column>
      </el-table>


    </div>

    <el-dialog :visible.sync="dialogVisible" title="图片大图" :close-on-click-modal="true">
      <img :src="dialogImageUrl" style="width: 100%; height: auto; max-width: none; max-height: none;" />
    </el-dialog>


    <el-dialog title="信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="address" label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="名字">
          <el-input type="textarea" :rows="5" v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item prop="serviceCount" label="服务详情">
          <el-input type="textarea" :rows="5" v-model="form.serviceCount" autocomplete="off"></el-input>

        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="add">确 定</el-button>
      </div>
    </el-dialog>
    <div class="pagination">
      <el-pagination
          background
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Notice",
  data() {
    return {
      dialogImageUrl: '', // 对话框中的图片地址
      dialogVisible: false, // 控制对话框的显示
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入店铺名字', trigger: 'blur'},
        ],
        address: [
          {required: true, message: '请输入您的地址', trigger: 'blur'},
        ],
        serviceCount: [
          {required: true, message: '请输入服务内容', trigger: 'blur'},
        ]
      },
      ids: []
    }
  },
  mounted() {
    this.load();
  },

  methods: {
    showFormDialog() {
      // 点击注册按钮时触发，显示弹出框
      this.fromVisible = true;
    },
    loadData() {
      // 重新加载数据的逻辑，例如调用 this.load(1) 或者其他方法
      this.load(1);
    },
    add(){
      this.$refs.formRef.validate((valid) =>{
        if(valid){
          axios.post("/provider/addservice", this.form)
              .then((response) =>{
                console.log(this.form)
                const result = response.data;
                console.log(result);
                if(result.code === 200){
                  this.$message.success("注册成功")
                  this.fromVisible = false;
                  this.loadData();
                }else {
                  this.$message.error("添加失败，该地址已存在");
                }

              })}})},
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      axios.get('/provider/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,

        },
      })
          .then((res) => {
            console.log('接口返回的数据:', res);
            this.tableData = res.data.data.list;
            this.total = res.data.data.total;
          })
          .catch((error) => {
            console.error('加载数据时出错:', error);
          });
    },showLargeImage(imageUrl) {
      // 在这里实现显示图片大图的逻辑，使用对话框组件显示大图
      this.dialogImageUrl = require('@/assets/imgs/bg1.jpg'); // 设置图片地址
      this.dialogVisible = true; // 打开对话框
    },

    handleCurrentChange(pageNum) {
      this.load(pageNum); // 调用加载数据方法
    },


  }}


</script>


<style scoped>
.container {
  padding: 20px; /* Add padding to the container */
  background-color: #f5f5f5; /* Set background color */
  border-radius: 10px; /* Add border radius for a rounded look */
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Add shadow for depth */
}

.search {
  margin-bottom: 20px; /* Add margin at the bottom */
  text-align: center; /* Center align the text */
}

.search p {
  font-family: Arial, sans-serif; /* Set font family */
  font-size: 20px; /* Set font size */
  color: #333; /* Set font color */
}
</style>
