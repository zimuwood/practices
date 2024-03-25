import { ProDescriptions , ProCard} from '@ant-design/pro-components';
import RcResizeObserver from 'rc-resize-observer';
import {Image} from "antd";
import {currentUser} from "@/services/ant-design-pro/api";
import {useEffect, useState} from "react";
import {DEFAULT_AVATAR} from "@/constants/constant";
export default () => {
  const [dataSource, setDataSource] = useState<any>([]);
  const [ ,setResponsive] = useState(false);
  useEffect(() => {
    const fetchData = async () => {
      const userRes = await currentUser();
      setDataSource(userRes.data);
    };
    fetchData();
  }, []);
  const myLabelStyle = {fontSize: 14, fontWeight: "bold"}
  const myItemStyle = {fontSize: 14}

  return (
    <>
      <RcResizeObserver
        key="resize-observer"
        onResize={(offset) => {
          setResponsive(offset.width < 596);
        }}
      >
        <ProCard
          bordered
          headerBordered
          split={'horizontal'}
        >
          <ProCard style={{fontSize: 40, fontWeight: "bold"}}>
            {dataSource.username} 的个人中心
          </ProCard>
          <ProCard>
            <ProDescriptions>
              <ProDescriptions.Item
                label="用户名"
                valueType="text"
                copyable={true}
                labelStyle={myLabelStyle}
              >
                <span style={myItemStyle}>{dataSource.username}</span>
              </ProDescriptions.Item>
            </ProDescriptions>
          </ProCard>
          <ProCard>
            <ProDescriptions>
              <ProDescriptions.Item
                label="账号"
                valueType="text"
                copyable={true}
                tooltip={"账号是唯一的登录凭证"}
                labelStyle={myLabelStyle}
              >
                <span style={myItemStyle}>{dataSource.userAccount}</span>
              </ProDescriptions.Item>
            </ProDescriptions>
          </ProCard>
          <ProCard>
            <ProDescriptions>
              <ProDescriptions.Item
                label="头像"
                valueType="avatar"
                labelStyle={myLabelStyle}
              >
                <Image src={dataSource.avatarUrl} width={150} height={150} fallback={DEFAULT_AVATAR} defaultValue={DEFAULT_AVATAR}/>
              </ProDescriptions.Item>
            </ProDescriptions>
          </ProCard>
          <ProCard>
            <ProDescriptions>
              <ProDescriptions.Item
                label="性别"
                valueType="text"
                labelStyle={myLabelStyle}
              >
                <span style={myItemStyle}>{dataSource.gender == 0 ? "男" : "女"}</span>
              </ProDescriptions.Item>
            </ProDescriptions>
          </ProCard>
          <ProCard>
            <ProDescriptions>
              <ProDescriptions.Item
                label="电话"
                valueType="text"
                labelStyle={myLabelStyle}
              >
                <span style={myItemStyle}>{dataSource.phone ?? "未填写"}</span>
              </ProDescriptions.Item>
            </ProDescriptions>
          </ProCard>
          <ProCard>
            <ProDescriptions>
              <ProDescriptions.Item
                label="邮箱"
                valueType="text"
                labelStyle={myLabelStyle}
              >
                <span style={myItemStyle}>{dataSource.email ?? "未填写"}</span>
              </ProDescriptions.Item>
            </ProDescriptions>
          </ProCard>
          <ProCard>
            <ProDescriptions>
              <ProDescriptions.Item
                label="创建时间"
                valueType="date"
                labelStyle={myLabelStyle}
                style={{
                  fontSize: 20,
                  color: "#666"
                }}
              >
                {dataSource.createTime}
              </ProDescriptions.Item>
            </ProDescriptions>
          </ProCard>
        </ProCard>
      </RcResizeObserver>
      <ProCard/>
    </>
  );
};
