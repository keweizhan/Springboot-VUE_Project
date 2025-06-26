<script setup lang="ts">
import {logout} from "@/net"
import router from "@/router"
function userLogout(){
  logout( ()=> router.push("/"))
}
</script>

<template>
  <div>
    <el-button @click="userLogout">Log out</el-button>
  </div>
</template>

<style scoped>

</style>