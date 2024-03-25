import type {ActionType, ProColumns, ProFormInstance} from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';
import {Image, message} from 'antd';
import {useRef} from 'react';
import {searchUsers} from "@/services/ant-design-pro/api";
import {DEFAULT_AVATAR} from "@/constants/constant";
import {API} from "@/services/ant-design-pro/typings";
export const waitTimePromise = async (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export const waitTime = async (time: number = 100) => {
  await waitTimePromise(time);
};

const columns: ProColumns<API.CurrentUser>[] = [
  {
    dataIndex: 'id',
    valueType: 'indexBorder',
    width: 48,
  },
  {
    title: '用户名',
    dataIndex: 'username',
    copyable: true,
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
    dataIndex: 'userAccount',
    copyable: true,
    ellipsis: true,
    formItemProps: {
      rules: [
        {
          pattern: new RegExp('^\\d{0,10}$'),
          message: '账号只能包含数字，且最多输入10位'
        }
      ]
    }
  },
  {
    title: '头像',
    dataIndex: 'avatarUrl',
    search: false,
    render: (_, record) => (
      <div>
        <Image src={record.avatarUrl} width={100}  alt={'图片失踪了'}  fallback={DEFAULT_AVATAR} defaultValue={DEFAULT_AVATAR}/>
      </div>
    ),
  },
  {
    title: '性别',
    dataIndex: 'gender',
    valueEnum: {
      0: { text: '男'},
      1: { text: '女'},
    }
  },
  {
    title: '电话',
    dataIndex: 'phone',
    copyable: true,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    copyable: true,
    ellipsis: true,
    tooltip: '邮箱过长会自动收缩',
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
    title: '状态',
    dataIndex: 'userStatus',
    valueEnum: {
      0: { text: '正常使用', color: 'blue'},
      1: { text: '被删除', status: 'error'},
    },
    search: false,
  },
  {
    title: '角色',
    dataIndex: 'userRole',
    search: false,
    valueEnum: {
      0: { text: '普通用户', status: 'default'},
      1: { text: '管理员', status: 'success'},
    }
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    valueType: 'date',
    search: false,
  },
];

export default () => {
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  return (
    <ProTable<API.CurrentUser>
      columns={columns}
      actionRef={actionRef}
      formRef={formRef}
      formData={formRef.current?.getFieldsValue()}
      cardBordered
      // @ts-ignore
      request={async (params, sort, filter) => {
        console.log(sort, filter);
        const userListRes = await searchUsers(formRef.current?.getFieldsValue() as API.SearchParams);
        if (userListRes.code == '10000') {
          return {
            data: userListRes.data
          }
        } else {
          message.error(userListRes.message)
          throw new Error(userListRes.message)
        }
      }}
      editable={{
        type: 'multiple',
      }}
      columnsState={{
        persistenceKey: 'pro-table-singe-demos',
        persistenceType: 'localStorage',
        defaultValue: {
          option: { fixed: 'right', disable: true },
        },
        onChange(value) {
          console.log('value: ', value);
        },
      }}
      rowKey="id"
      search={{
        labelWidth: 'auto',
      }}
      options={{
        setting: {
          // @ts-ignore
          listsHeight: 400,
        },
      }}
      form={{ ignoreRules: false }}
      pagination={{
        pageSize: 5,
        onChange: (page) => console.log(page),
      }}
      dateFormatter="string"
      headerTitle="高级表格"
    />
  );
};
