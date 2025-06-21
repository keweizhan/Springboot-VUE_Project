<script setup lang="ts">

</script>

<template>
  <div style = "width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex: 1;background-color: black">
      <el-image style="width: 100%;height: 100%" fit="cover"
        src="https://i0.wp.com/handluggageonly.co.uk/wp-content/uploads/2015/11/Sunset-in-Lofoten.jpg?resize=1280%2C854&ssl=1"/>
    </div>
    <div class="Welcome-title">
      <div style="font-size: 30px;font-weight: bold">Welcome to our platform!</div>
    </div>
    <div class="right-card">
      <router-view/>
    </div>
  </div>
</template>

<style scoped>
.Welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
.right-card{
  width: 400px;
  z-index: 1;
  background-color: white;
}

</style>