import Footer from '@/components/Footer';
import { login } from '@/services/ant-design-pro/api';
import {
  LockOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {
  LoginForm,
  ProFormCheckbox,
  ProFormText,
} from '@ant-design/pro-components';
import {Alert, Divider, message, Tabs} from 'antd';
import React, { useState } from 'react';
import {history, Link, useModel} from 'umi';
import styles from './index.less';
import {RESET_PWD} from "@/constants/constant";
import {API} from "@/services/ant-design-pro/typings";
const LoginMessage: React.FC<{
  content: string;
}> = ({ content }) => (
  <Alert
    style={{
      marginBottom: 24,
    }}
    message={content}
    type="error"
    showIcon
  />
);
const Login: React.FC = () => {
  const [userLoginState] = useState<API.LoginResult>({});
  const [type, setType] = useState<string>('account');
  const { initialState, setInitialState } = useModel('@@initialState');
  const fetchUserInfo = async () => {
    const res = await initialState?.fetchUserInfo?.();
    if (res?.data) {
      await setInitialState((s) => ({
        ...s,
        currentUserRes: res,
      }));
    }
  };
  const handleSubmit = async (values: API.LoginParams) => {
    try {
      // 登录
      const res = await login({
        ...values,
        type,
      });
      if (res.code == '10000') {
        const defaultLoginSuccessMessage = '登录成功！';
        message.success(defaultLoginSuccessMessage);
        await fetchUserInfo();
        /** 此方法会跳转到 redirect 参数所在的位置 */
        if (!history) return;
        const { query } = history.location;
        const { redirect } = query as {
          redirect: string;
        };
        history.push(redirect || '/');
        return;
      } else {
        throw new Error(res.description)
      }
    } catch (error: any) {
      const defaultLoginFailureMessage = '登录失败，请重试！';
      message.error(error.message ?? defaultLoginFailureMessage);
    }
  };
  const { status, type: loginType } = userLoginState;
  const forgetPWD = () => {
    message.error("请联系管理员cloudyW");
  }
  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <LoginForm
          logo={<img alt="logo" src="/logo.svg" />}
          title="用户管理系统"
          subTitle={
          <>
            <p>手搓的第一个项目</p>
          </>
        }
          initialValues={{
            autoLogin: true,
          }}
          onFinish={async (values) => {
            await handleSubmit(values as API.LoginParams);
          }}
        >
          <Tabs activeKey={type} onChange={setType}>
            <Tabs.TabPane key="account" tab={'账号密码登录'} />
          </Tabs>

          {status === 'error' && loginType === 'account' && (
            <LoginMessage content={'错误的账号和密码'} />
          )}
          {type === 'account' && (
            <>
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
                }}
                placeholder={'请输入账号'}
                rules={[
                  {
                    required: true,
                    message: '账号是必填项！',
                  },
                  {
                    pattern: new RegExp('^\\d{10}$'),
                    message: '账号只能是10位且是纯数字',
                  },
                ]}
              />
              <ProFormText.Password
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon} />,
                }}
                placeholder={'请输入密码'}
                rules={[
                  {
                    required: true,
                    message: '密码是必填项！',
                  },
                  {
                    pattern: new RegExp('^\\w{6,16}$'),
                    message: '密码不能包含特殊字符，且需要是6-16位',
                  },
                ]}
              />
            </>
          )}
          <div
            style={{
              marginBottom: 24,
            }}
          >
            <ProFormCheckbox noStyle name="autoLogin">
              自动登录
            </ProFormCheckbox>
            <Divider type="vertical" />
            <Link to="/user/register" >还没有账号 ? 点击注册</Link>
            <Divider type="vertical" />
            <a
              style={{
                float: 'right',
              }}
              href={RESET_PWD}
              onClick={forgetPWD}
            >
              忘记密码 ?
            </a>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};
export default Login;
