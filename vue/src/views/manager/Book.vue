<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入服务店铺名称查询" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="search">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
<!--//评价弹出框-->
      <el-dialog :visible.sync="evaluateVisible" title="评价弹出框">
        <el-form :model="formData" ref="evaluateForm" label-width="100px">
          <el-form-item label="地址" prop="address">
            <el-input v-model="formData.address" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input v-model="formData.name" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item prop="image" label="图片" >
            <template slot-scope="scope">
              <img :src="require('@/assets/imgs/bg1.jpg')" style="width: 150px; height: 150px;" alt="图片">
            </template>
          </el-form-item>
          <el-form-item label="评分" prop="rating" class="start">
            <el-rate v-model="formData.rating" :max="5"></el-rate>
          </el-form-item>
          <el-form-item label="评价内容" prop="comment">
            <el-input type="textarea" v-model="formData.comment"></el-input>
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="evaluateVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">提交评价</el-button>
        </div>
      </el-dialog>


    </div>
<!--主页面数据-->
    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="providerName" label="店铺名字" show-overflow-tooltip></el-table-column>
        <el-table-column prop="author" label="评价者" show-overflow-tooltip>用户</el-table-column>
        <el-table-column prop="rating" label="评分" show-overflow-tooltip></el-table-column>
        <el-table-column prop="comment" label="评论" show-overflow-tooltip></el-table-column>

        <el-table-column label="操作" width="280" align="center">
          <template v-slot="scope">

            <el-button plain type="primary" @click="details(scope.row.providerName)" size="mini">详情</el-button>
            <el-button plain type="primary" @click="settrust(scope.row.providerName)" size="mini">设置</el-button>
          </template>
        </el-table-column>
      </el-table>


    </div>

<!--    详情页面-->
    <el-dialog :visible.sync="evaluateVisible1" title="评价弹出框">
      <el-form :model="formData1" ref="evaluateForm" label-width="100px">
        <el-form-item label="地址" prop="address">
          <el-input v-model="formData1.address" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="formData1.name" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="image" label="图片" >
          <template slot-scope="scope">
            <img :src="require('@/assets/imgs/bg1.jpg')" style="width: 150px; height: 150px;" alt="图片">
          </template>
        </el-form-item>
        <el-form-item label="信任状态" prop="trusted">
          <el-input v-model="formData1.trusted" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="总评分" prop="totalRating" :disabled="true">
          <el-input v-model="formData1.totalRating" ></el-input>
        </el-form-item>
        <el-form-item label="评价个数" prop="totalRatingsCount" :disabled="true">
          <el-input  v-model="formData1.totalRatingsCount" ></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="evaluateVisible = false">取消</el-button>
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
  title: "book",

  data() {
    return {
      tableData: [],
      formData: {
        address: '', // 地址
        name: '', // 名称
        rating: 0, // 评分
        comment: '' // 评价内容
      },
      formData1: {
        address: '', // 地址
        name: '', // 名称
        trusted: '', //
        totalRating: '', // 评价内容
        totalRatingsCount: '',
      },

      evaluateVisible: false, // 控制评价弹出框的显示
      evaluateVisible1:false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入服务商名称', trigger: 'blur'},
        ],
      },
      ids: [],
      headerData: [],
      memo: "",
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    handleAdd() {
      this.form = {};
      this.fromVisible = true;
    },
    loadData() {
      // 重新加载数据的逻辑，例如调用 this.load(1) 或者其他方法
      this.load(1);
    },
    search(){
      const name = this.name;

      console.log(name)
      axios.get(`/provider/getserviceInfo/${name}`)
          .then(response =>{
            const result = response.data;
            console.log(result);
            if (result.code === 200){
              this.formData.address = result.data.address;
              this.formData.name = result.data.name;
              // 显示弹出框
              console.log(name)
              this.evaluateVisible = true;
            }
          })
    },
    details(providerName){

      console.log(providerName)
      axios.get(`/provider/getserviceInfo/${providerName}`)
          .then(response =>{
            const result = response.data;
            console.log(result);
            if (result.code === 200){
              this.formData1.address = result.data.address;
              this.formData1.name = result.data.name;
              this.formData1.trusted = result.data.trusted;
              this.formData1.totalRating = result.data.totalRating;
              this.formData1.totalRatingsCount= result.data.totalRatingsCount;
              // 显示弹出框
              console.log(providerName)
              this.evaluateVisible1 = true;
            }
          })
    },
    settrust(){
      

    },
    submitForm(){
      const evaluationData = {
        providerName: this.formData.name,
        rating: this.formData.rating,
        comment: this.formData.comment
      };
      // 发起后端请求
      axios.post('/provider/submit', evaluationData)
          .then(response => {
            const result = response.data;
            if (result.code === 200) {
              // 如果评价提交成功，显示成功提示，并清空表单数据
              this.$message.success('评价成功');
              this.evaluateVisible = false; // 隐藏评价弹出框
              this.resetForm(); // 清空表单数据
              this.load(1);
            } else {
              // 如果评价提交失败，显示失败提示
              this.$message.error('评价失败');
            }
          })

    },
    handleSelectionChange(rows) {
      this.ids = rows.map((v) => v.id);
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      axios.get('/provider/selectPage2', {
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
    },
    reset() {
      // 清空搜索框内容
      this.name = null;
      // 清空表格数据
      this.tableData = [];
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum); // 调用加载数据方法
    },

    resetForm() {
      this.formData = {
        address: '',
        name: '',
        rating: 0,
        comment: ''
      };
    }


  }
}
</script>


<style scoped>

</style>
