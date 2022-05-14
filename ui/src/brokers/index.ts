// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { ResourceProps } from 'react-admin';

import icons from '../icons';

import BrokerList from './BrokerList';
import BrokerCreate from './BrokerCreate';
import BrokerEdit from './BrokerEdit';

const BrokerResourceProps: ResourceProps = {
    name: 'brokers',
    list: BrokerList,
    create: BrokerCreate,
    edit: BrokerEdit,
    icon: icons.brokers,
};

export {
    BrokerResourceProps,
};
