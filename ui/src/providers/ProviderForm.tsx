// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    TabbedForm,
} from 'react-admin';

import { ConfigurationFormTab } from '../entities';
import DatacenterFormTab from './DatacenterFormTab';

const ProviderForm = ({ toolbar }): JSX.Element => (
    <TabbedForm toolbar={toolbar}>
        <ConfigurationFormTab />
        <DatacenterFormTab />
    </TabbedForm>
);

export default ProviderForm;
