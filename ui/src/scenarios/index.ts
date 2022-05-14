// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { ResourceProps } from 'react-admin';
import icons from '../icons';

import ScenarioList from './ScenarioList';
import ScenarioCreate from './ScenarioCreate';
import ScenarioEdit from './ScenarioEdit';

const ScenarioResourceProps: ResourceProps = {
    name: 'scenarios',
    list: ScenarioList,
    create: ScenarioCreate,
    edit: ScenarioEdit,
    icon: icons.configurations,
};

export {
    ScenarioResourceProps,
};
