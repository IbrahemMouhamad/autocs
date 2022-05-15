// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Create,
} from 'react-admin';

import RunForm from './RunForm';

const RunCreate = ({ scenario, mutationOptions, ...props }): JSX.Element => (
    <Create
        title='label.run.create.title'
        mutationOptions={mutationOptions}
        {...props}
    >
        <RunForm scenario={scenario} />
    </Create>
);

export default RunCreate;
