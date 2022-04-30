// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { ResourceProps } from 'react-admin';
import icons from '../icons';

import ExperimentList from './ExperimentList';
import ExperimentCreate from './ExperimentCreate';

const ExperimentResourceProps: ResourceProps = {
    name: 'experiments',
    list: ExperimentList,
    create: ExperimentCreate,
    icon: icons.configurations,
};

export {
    ExperimentResourceProps,
};
