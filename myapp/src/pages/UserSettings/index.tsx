import type {ActionType} from '@ant-design/pro-components';
import {ProCard, ProDescriptions, ProForm} from '@ant-design/pro-components';
import {Button, message, Upload} from 'antd';
import {useEffect, useRef, useState} from 'react';
import {currentUser, updateUserInfo} from "@/services/ant-design-pro/api";
import {history} from "@@/core/history";
import { SmileOutlined } from '@ant-design/icons';

export default () => {
  const actionRef = useRef<ActionType>();
  const [form] = ProForm.useForm();
  const [isDisabled, setIsDisabled] = useState(true);
  const [avatar, setAvatar] = useState<any>([]);
  const [dataSource, setDataSource] = useState<any>([]);
  const [updateInfo, setUpdateInfo] = useState<any>([]);
  const [, setFileList] = useState([]);
  useEffect(() => {
    const fetchData = async () => {
      const userRes = await currentUser();
      setDataSource(userRes.data);
    };
    fetchData();
  }, []);
  // @ts-ignore
  const handleChange = ({ fileList: newFileList }) => {
    setFileList(newFileList);
    // 使用map来提取所有成功上传文件的响应，然后使用every来检查是否所有文件都上传成功
    const firstFileResponse = newFileList[0]?.response;
    if (newFileList.length > 0 && newFileList.some((file: { response: any; }) => file.response)) {
      const res = firstFileResponse;
      if (res.code == '10000') {
        setAvatar(firstFileResponse.data);
        message.success('文件上传成功！');
        setIsDisabled(false);
      }
      else {
        message.error(firstFileResponse.description);
      }
    }
  }
  const handleFieldSave = (field: any, value: string) => {
    // 在这里可以执行一些自定义的逻辑，比如更新数据、发送请求等
    console.log(`字段 ${field} 的值已保存为 ${value}`);
  };
  const handleUpdate = async () => {
    try {
      setUpdateInfo(dataSource);
      dataSource.avatar = avatar;
      const res = await updateUserInfo(updateInfo);
      if (res.code == '10000') {
        const defaultUpdateSuccessMessage = '修改成功！';
        message.success(defaultUpdateSuccessMessage);
        /** 此方法会跳转到 redirect 参数所在的位置 */
        if (!history) return;
        const {query} = history.location;
        history.push({
          pathname: '/user-center',
          query
        });
        return;
      } else {
        throw new Error(res.description)
      }
    } catch (error: any) {
      const defaultUpdateFailureMessage = '修改信息失败';
      message.error(error.message ?? defaultUpdateFailureMessage);
    }
  }
  return (
    <ProCard>
      <ProDescriptions
        actionRef={actionRef}
        title="编辑个人信息"
        labelStyle={{fontSize: 14, fontWeight: "bold"}}
        dataSource={dataSource}
        editable={{
          // @ts-ignore
          onSave: (key, value) => {
            let initVal;
            let realVal;
            if (key == 'username') {
              initVal = dataSource.username;
              dataSource.username = value.username;
              realVal = value.username;
            } else if (key == 'gender') {
              initVal = dataSource.gender;
              dataSource.gender = value.gender;
              realVal = value.gender;
            } else if (key == 'phone') {
              initVal = dataSource.phone;
              dataSource.phone = value.phone;
              realVal = value.phone;
            } else if (key == 'email') {
              initVal = dataSource.email;
              dataSource.email = value.email;
              realVal = value.email;
            }
            if (initVal != realVal) {
              setIsDisabled(false);
            }
            return handleFieldSave(key, realVal);
          }
        }}
        column={2}
        columns={[
          {
            title: '用户名',
            key: 'username',
            valueType: 'text',
            dataIndex: 'username',
            render: (text) => text || "出错了",
            formItemProps: {
              rules: [
                {
                  pattern: new RegExp('^\\w{4,16}$'),
                  message: '用户名不能包含特殊字符，且需要是4-16位',
                },
              ]
            }
          },
          {
            title: '账号',
            key: 'account',
            valueType: 'text',
            dataIndex: 'userAccount',
            editable: false,
            render: (text) => text || "出错了",
          },
          {
            title: '头像',
            key: 'avatar',
            valueType: 'image',
            tooltip: '只支持 jpg/jpeg/png/webp 格式',
            render: () => {
              return (
                <ProForm form={form}
                  submitter={{
                    submitButtonProps: { style: {display: 'none'} },
                    resetButtonProps: { style: {display: 'none'} }
                }}
                >
                  <ProForm.Item name="image">
                    <Upload
                      name="image"
                      maxCount={1}
                      listType="picture-card"
                      className="avatar-uploader"
                      action="api/user/getAvatar"
                      onChange={handleChange}
                      accept={"image/jpg,image/jpeg,image/png,image/webp"}
                    >
                      {dataSource.avatarUrl != undefined ? <img src={dataSource.avatarUrl} alt="avatar" style={{ width: '100%' }} /> : <SmileOutlined style={{ fontSize: '48px', color: 'grey' }} />}
                    </Upload>
                  </ProForm.Item>
                </ProForm>
              )
            },
            editable: false,
          },
          {
            title: '性别',
            key: 'gender',
            dataIndex: 'gender',
            valueType: 'select',
            valueEnum: {
              0: {
                text: '男',
              },
              1: {
                text: '女',
              },
            },
          },
          {
            title: '电话',
            key: 'phone',
            valueType: 'text',
            dataIndex: 'phone',
            render: (text) => text || "未填写",
          },
          {
            title: '邮箱',
            key: 'email',
            valueType: 'text',
            dataIndex: 'email',
            render: (text) => text || "未填写",
            tooltip: "请务必填写正确的邮箱以便找回密码",
            formItemProps: {
              rules: [
                {
                  pattern: new RegExp('\\w+@\\w+(\\.\\w+)+'),
                  message: '请填写正确的邮箱'
                }
              ]
            }
          },
          {
            title: '创建时间',
            key: 'createTime',
            dataIndex: 'createTime',
            valueType: 'date',
            editable: false,
          },
          {
            title: '操作',
            valueType: 'option',
            render: () => [
              <Button type="primary" disabled={isDisabled} title="确认修改" onClick={handleUpdate}>确认修改</Button>
            ],
          },
        ]}
       />
    </ProCard>
  );
};
