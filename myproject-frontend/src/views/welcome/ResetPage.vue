<script setup lang="ts">
import {ref,reactive} from "vue";
import {EditPen, Lock, Message} from "@element-plus/icons-vue"

const form = reactive({
  email: "",
  code: "",
  password: "",
  password_confirmation: ""
})

const active = ref(0)
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
        <el-form :model="form">
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
                <el-button type="success" >
                  Get Verification Code
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button @click="active++" style="width: 270px" type = "warning" plain>Reset Password</el-button>
      </div>
    </div>
    <div style="margin:0 20px" v-if="active === 1">
      <div style="margin-top: 80px">
        <div style="font-size: 25px; font-weight: bold">Reset Password</div>
        <div style="font-size: 14px; color: grey">Please enter your new password.</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form">
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
        <el-button @click="active++" style="width: 270px" type = "danger" plain>Reset Password</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>