<script setup lang="ts">

import {EditPen, Lock, Message} from "@element-plus/icons-vue"
import {ElMessage} from "element-plus";
import {get, post} from "@/net";
import {computed, reactive, ref} from "vue";
import router from "@/router";

const form = reactive({
  email: "",
  code: "",
  password: "",
  password_confirmation: ""
})

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('Please enter a valid username'));
  } else if(!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)){
    callback(new Error('Username can not include special characters'));
  } else {
    callback()
  }
}

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('Please enter a valid password'));
  } else if (value !== form.password) {
    callback(new Error("Passwords do not match."))
  } else {
    callback()
  }
}

const rule = {
  username: [
    { validator: validateUsername, trigger: ['blur', 'change'] },
    { min: 2, max: 8, message: 'Username must be 2–8 characters.', trigger: ['blur', 'change'] },
  ],
  password: [
    { required: true, message: 'Please enter the Password', trigger: 'blur' },
    { min: 6, max: 16, message: 'Password length must be 6–16 characters.', trigger: ['blur', 'change'] }
  ],
  password_confirmation: [
    { validator: validatePassword, trigger: ['blur', 'change'] },
  ],
  email: [
    { required: true, message: 'Please enter your email', trigger: 'blur' },
    {type: 'email', message: 'Please enter a valid email address.', trigger: ['blur', 'change']}
  ],
  code: [
    { required: true, message: 'Please enter the verification code you received.', trigger: 'blur' },
  ]
}



const formRef = ref()
const coldTime = ref(0)

function askCode() {
  coldTime.value = 60
  if (isEmailValid){
    get(`/api/auth/ask-code?email=${form.email}&type=register`, () => {
      ElMessage.success(`The verification code has been sent to your email: ${form.email}. Please check your email.`)
      setInterval(() => coldTime.value--, 1000)
    }, (message) => {ElMessage.warning(message)
      coldTime.value = 0})
  }

}
const active = ref(0)

const isEmailValid = computed(() => /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/.test(form.email))
function confirmReset() {
  formRef.value.validate(valid => {
    if (valid) {
      post(`/api/auth/reset-confirm`, {
        email: form.email,
        code: form.code
      }, ()=> active.value++)
    }
  })
}

function doReset() {
  formRef.value.validate(valid => {
    if (valid) {
      post(`/api/auth/reset-password`, {...form}, () =>{
        ElMessage.success(`Password reset successfully. Please Log in again.`)
        router.push('/')
      })
    }
  })
}

</script>

<template>
  <div style="text-align: center">
    <div style="margin-top: 30px">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="Verify Email"/>
        <el-step title="Reset Password"/>
      </el-steps>
    </div>
    <div style="margin:0 20px" v-if="active === 0">
      <div style="margin-top: 80px">
        <div style="font-size: 25px; font-weight: bold">Reset Password</div>
        <div style="font-size: 14px; color: grey">Please enter the email address to reset your password.</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="email">
            <el-input v-model="form.email" type="email" placeholder="Email">
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>/
            </el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-row :gutter="10" style="width: 100%;">
              <el-col :span="17">
                <el-input v-model="form.code" maxlength="6" type="text" placeholder="Please Enter the Verification Code">
                  <template #prefix>
                    <el-icon><EditPen /></el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="5">
                <el-button type="success" @click="askCode" :disabled="!isEmailValid || coldTime > 0">
                  {{coldTime > 0 ? 'Please wait ' + coldTime + ' seconds' : 'Get Verification Code'}}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button @click="confirmReset" style="width: 270px" type = "warning" plain>Reset Password</el-button>
      </div>
    </div>
    <div style="margin:0 20px" v-if="active === 1">
      <div style="margin-top: 80px">
        <div style="font-size: 25px; font-weight: bold">Reset Password</div>
        <div style="font-size: 14px; color: grey">Please enter your new password.</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="password">
            <el-input v-model="form.password" maxlength="20" type="password" placeholder="Password">
              <template #prefix>
                <el-icon><Lock/></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password_confirmation">
            <el-input v-model="form.password_confirmation" maxlength="20" type="password" placeholder="Confirm Password">
              <template #prefix>
                <el-icon><Lock/></el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button @click="doReset" style="width: 270px" type = "danger" plain>Reset Password</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>