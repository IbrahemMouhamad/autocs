// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { DefaultListPage, GridActionToolbar } from '../common';

const ScenarioList = (): JSX.Element => (
    <DefaultListPage rowClick='show'>
        <GridActionToolbar DetailsComponent={null} />
    </DefaultListPage>
);

export default ScenarioList;
