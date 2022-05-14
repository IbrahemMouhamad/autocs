// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    TabbedForm,
} from 'react-admin';

import { ConfigurationFormTab } from '../entities';

import ProviderFormTab from './ProviderFormTab';

const ScenarioForm = ({ toolbar }): JSX.Element => (
    <TabbedForm toolbar={toolbar}>
        <ConfigurationFormTab />
        <ProviderFormTab />
    </TabbedForm>
);

export default ScenarioForm;
