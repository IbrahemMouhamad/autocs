// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Create, SaveButton, Toolbar,
} from 'react-admin';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';

import { CreatePageToolbar } from '../common';

import ScenarioForm from './ScenarioForm';

const ScenarioCreateToolbar = (props): JSX.Element => (
    <Toolbar {...props}>
        <SaveButton
            label='action.continue'
            icon={<ArrowRightIcon />}
        />
    </Toolbar>
);

const ScenarioCreate = (props): JSX.Element => (
    <Create
        actions={<CreatePageToolbar />}
        redirect='edit'
        mutationMode='pessimistic'
        {...props}
    >
        <ScenarioForm toolbar={<ScenarioCreateToolbar />} {...props} />
    </Create>
);

export default ScenarioCreate;
