// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Create,
} from 'react-admin';

import ExperimentForm from './ExperimentForm';

const ExperimentCreate = (props): JSX.Element => (
    <Create {...props} redirect='list'>
        <ExperimentForm {...props} />
    </Create>
);

export default ExperimentCreate;
