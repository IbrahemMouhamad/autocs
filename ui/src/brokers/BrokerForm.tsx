// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    TabbedForm,
} from 'react-admin';

import { ConfigurationFormTab } from '../entities';

import VmFormTab from './VmFormTab';
import CloudletFormTab from './CloudletFormTab';

const BrokerForm = ({ toolbar }): JSX.Element => (
    <TabbedForm toolbar={toolbar}>
        <ConfigurationFormTab />
        <VmFormTab />
        <CloudletFormTab />
    </TabbedForm>
);

export default BrokerForm;
