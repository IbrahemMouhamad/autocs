// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { ResourceProps } from 'react-admin';
import icons from '../icons';

import RunCreate from './RunCreate';
import RunList from './RunList';
import RunShow from './RunShow';

const RunResourceProps: ResourceProps = {
    name: 'runs',
    list: RunList,
    show: RunShow,
    icon: icons.runs,
};

export {
    RunResourceProps,
    RunCreate,
};
