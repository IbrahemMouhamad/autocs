// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Edit,
} from 'react-admin';

import { EditPageToolbar } from '../common';

import ScenarioForm from './ScenarioForm';

const ScenarioEdit = (props): JSX.Element => (
    <Edit
        actions={<EditPageToolbar />}
        redirect='list'
        mutationMode='pessimistic'
        {...props}
    >
        <ScenarioForm {...props} />
    </Edit>
);

export default ScenarioEdit;
