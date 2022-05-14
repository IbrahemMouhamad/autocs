// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import SettingsIcon from '@mui/icons-material/Settings';

import { FormTab } from '../common';

import BasicConfigurationForm from './BasicConfigurationForm';
import AdvancedConfigurationForm from './AdvancedConfigurationForm';

const ConfigurationFormTab = (props): JSX.Element => (
    <FormTab label='label.provider.tabs.general' icon={<SettingsIcon />} {...props}>
        <BasicConfigurationForm />
        <AdvancedConfigurationForm />
    </FormTab>
);

export default ConfigurationFormTab;
