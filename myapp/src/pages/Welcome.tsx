import { PageContainer } from '@ant-design/pro-components';
import { Alert, Card } from 'antd';
import React from 'react';
import {currentUser} from "@/services/ant-design-pro/api";

const user = await currentUser()

const Welcome: React.FC = () => {
  return (
    <PageContainer>
      <Card>
        <Alert
          message={'登录成功! 欢迎: ' + user?.data.username}
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
            marginBottom: 24,
            fontSize: '30px',
          }}
        />
      </Card>
    </PageContainer>
  );
};
export default Welcome;
