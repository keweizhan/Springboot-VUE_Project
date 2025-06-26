import axios from "axios";
import {ElMessage} from "element-plus";

const authItemName = "access_token"

const defaultFailure = (message, code, url) => {
    console.warn(`Request address: ${url}, code: ${code}, Error: ${message}`)
    ElMessage.warning(message)
}
const defaultError = (err) => {
    console.error(err)
    ElMessage.warning("An error has occurred. Please contact the administrator.")
}

function takeAccessToken() {
    const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName)
    if (!str) return null;
    const authToken = JSON.parse(str)
    if (authToken.expire <= new Date()){
        deleteAccessToken()
        ElMessage.warning("Your session has expired. Please log in again.")
        return null
    }
    return authToken.token
}


function storeAccessToken(token, remember, expire) {
    const authObj = {token: token, expire: expire}
    const str = JSON.stringify(authObj)
    if (remember) {
        localStorage.setItem(authItemName, str)
    } else {
        sessionStorage.setItem(authItemName, str)
    }
}

function accessHeader(){
    const token = takeAccessToken();
    return token ?{
        'Authorization': `Bearer ${token}`
    } : {}
}

function get(url, success, failure = defaultFailure) {
    internalGet(url, accessHeader(), success, failure)
}


function post(url, data, success, failure = defaultFailure) {
    internalPost(url, data,accessHeader(), success, failure)
}
function deleteAccessToken() {
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}
function internalPost(url, data,header,success,failure, error = defaultError){
    axios.post(url, data,{headers: header }).then(({data}) => {
        if(data.code === 200){
            success(data.data);
        } else{
            failure(data.msg, data.code, url);
        }
    }).catch(err => error(err))
}

function internalGet(url, header,success,failure, error = defaultError){
    axios.get(url, {headers: header }).then(({data}) => {
        if(data.code === 200){
            success(data.data);
        } else{
            failure(data.message, data.code, url);
        }
    }).catch(err => error(err))
}

function login(username,password,remember, success, failure = defaultFailure){
    internalPost('api/auth/login',{
        username: username,
        password: password
    },{
        'Content-Type': 'application/x-www-form-urlencoded'
    },(data) => {
        storeAccessToken(data.token, remember, data.expire);
        ElMessage.success(`Login successful!, Welcome, ${data.username}!`)
        success(data)
    }, failure);
}

function logout(success, failure = defaultFailure) {
    get('api/auth/logout', () => {
        deleteAccessToken()
        ElMessage.success("Logout successful!")
        success()
    }, failure)
}

function unauthorized() {
    return !takeAccessToken()
}

export {login, logout, get, post, unauthorized};