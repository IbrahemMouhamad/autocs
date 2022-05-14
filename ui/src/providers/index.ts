// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { ResourceProps } from 'react-admin';

import icons from '../icons';
import ProviderList from './ProviderList';
import ProviderCreate from './ProviderCreate';
import ProviderEdit from './ProviderEdit';

const ProviderResourceProps: ResourceProps = {
    name: 'providers',
    list: ProviderList,
    create: ProviderCreate,
    edit: ProviderEdit,
    icon: icons.providers,
};

export {
    ProviderResourceProps,
};
