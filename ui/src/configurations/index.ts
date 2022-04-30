// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { ResourceProps } from 'react-admin';
import icons from '../icons';
import ConfigurationList from './ConfigurationList';
import ConfigurationInputs from './ConfigurationInputs';

const ConfigurationResourceProps: ResourceProps = {
    name: 'configurations',
    list: ConfigurationList,
    icon: icons.configurations,
};

export {
    ConfigurationResourceProps,
    ConfigurationInputs,
};
