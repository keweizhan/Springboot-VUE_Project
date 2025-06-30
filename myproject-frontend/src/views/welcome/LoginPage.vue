<script setup lang="ts">
import {reactive, ref} from "vue";
import {User,Lock} from '@element-plus/icons-vue'
import {login} from '@/net'
import router from '@/router'
const form = reactive({
  username: "",
  password: "",
  remember: false
})
const formRef = ref()
const rule= {
  username: [
      {required: true, message: 'Please enter your Username', trigger: 'blur'}
  ],
  password: [
      {required: true, message: 'Please enter your password', trigger: 'blur'}
  ]
}
function userLogin(){
  formRef.value.validate((valid) => {
    if (valid) {
      login(form.username, form.password, form.remember,
          ()=> router.push('/index'))
    }
  })
}
</script>
<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold">Log in</div>
      <div style="font-size: 14px;color: gray"> Please Log in</div>
    </div>
    <div style="margin-top: 50px">
      <el-form :model="form" :rules="rule" ref = "formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username"  type="text" placeholder="Username/Email">
            <template #prefix>
              <el-icon><User/></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" maxlength="20" placeholder="Password">
            <template #prefix>
              <el-icon><Lock/></el-icon>
            </template>

          </el-input>
        </el-form-item>
        <el-row>
          <el-col :span="12" style="text-align: left">
            <el-form-item prop="remember">
              <el-checkbox v-model="form.remember" label="Remember me"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link @click="router.push('/reset')">Forget Password</el-link>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div style="margin-top: 40px">
      <el-button @click="userLogin" style="width: 270px" type="success" plain> Login</el-button>
    </div>
    <div>
      <el-divider>
        <span style="font-size: 13px;color: gray">Not a Member</span>
      </el-divider>
      <div>
        <el-button @click="router.push('/register')" style="width: 270px" type="warning" plain>Sign up</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>