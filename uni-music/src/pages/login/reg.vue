<template>
	<view class="content">
		<view class="input-group">
			<view class="input-row border">
				<text class="title">账号：</text>
				<m-input type="text" focus clearable v-model="username" placeholder="请输入账号"></m-input>
			</view>
			<view class="input-row border">
				<text class="title">密码：</text>
				<m-input type="password" displayable v-model="password" placeholder="请输入密码"></m-input>
			</view>
			<view class="input-row">
				<text class="title">确认密码：</text>
				<m-input type="password" displayable v-model="confirmPassword" placeholder="请确认密码"></m-input>
			</view>
		</view>
		<view class="btn-row">
			<button type="primary" class="primary" @tap="login">注册并登录</button>
		</view>
	</view>
</template>

<script>
	import mInput from '@/my-components/m-input/m-input.vue';
    import { registerUser, login } from '@/api/auth.js';

	export default {
		components: {
			mInput
		},
		data() {
			return {
				username: '',
				password: '',
				confirmPassword: ''
			}
		},
		methods: {
			register() {
				/**
				 * 客户端对账号信息进行一些必要的校验。
				 * 实际开发中，根据业务需要进行处理，这里仅做示例。
				 */
				if (this.username.length < 3) {
					uni.showToast({
						icon: 'none',
						title: '账号最短为 3 个字符'
					});
					return;
				}
				if (this.password.length < 6) {
					uni.showToast({
						icon: 'none',
						title: '密码最短为 6 个字符'
					});
					return;
				}
				if (this.password !== this.confirmPassword) {
					uni.showToast({
						icon: 'none',
						title: '两次密码输入不一致'
					});
					return;
				}

				const registerdata = {
					username: this.username,
					password: this.password,
                    repeatPassword: this.confirmPassword
				}
                //注册
                registerUser(registerdata).then(data => {
                    uni.showToast({
								title: '注册成功'
							});
                    //注册成功后登录
                    const logindata = {
                        username: this.username,
                        password: this.password
                    }
                    login(logindata).then(data => {
                        console.log(data.token)
                        console.log(data.user)
                        uni.setStorageSync('token', data.token)
                        uni.setStorageSync('user', user)
                        uni.reLaunch({
                            url: '../index/index',
                        });
                    })
                })
            },
            login() {
                //注册成功后登录
                const logindata = {
                    username: this.username,
                    password: this.password
                }
                login(logindata).then(data => {
                    console.log(data.token)
                    console.log(data.user)
                    uni.setStorageSync('token', data.token)
                    uni.setStorageSync('user', user)
                    uni.reLaunch({
                        url: '../index/index',
                    });
                })
            }
		}
	}
</script>

<style lang="scss" scoped>
@import "page.css";
</style>
