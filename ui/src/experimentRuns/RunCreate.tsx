// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Create,
} from 'react-admin';

import RunForm from './RunForm';

const RunCreate = ({ scenario, ...props }): JSX.Element => (
    <Create
        title='label.run.create.title'
        {...props}
    >
        <RunForm scenario={scenario} />
    </Create>
);

export default RunCreate;
