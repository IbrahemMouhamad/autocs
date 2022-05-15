// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { ResourceProps } from 'react-admin';
import icons from '../icons';

import RunCreate from './RunCreate';

const RunResourceProps: ResourceProps = {
    name: 'runs',
    icon: icons.configurations,
};

export {
    RunResourceProps,
    RunCreate,
};
