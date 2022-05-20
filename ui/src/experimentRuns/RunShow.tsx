// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { ShowBase } from 'react-admin';

import DatacenterMetrics from './DatacenterMetrics';

const RunShow = (): JSX.Element => (
    <ShowBase resource='runs'>
        <DatacenterMetrics />
    </ShowBase>
);

export default RunShow;
