// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Create,
} from 'react-admin';

import { CreatePageToolbar } from '../common';

import ScenarioForm from './ScenarioForm';

const ScenarioCreate = (props): JSX.Element => (
    <Create
        actions={<CreatePageToolbar />}
        redirect='edit'
        {...props}
    >
        <ScenarioForm {...props} />
    </Create>
);

export default ScenarioCreate;
