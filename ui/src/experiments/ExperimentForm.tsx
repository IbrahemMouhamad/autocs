// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Form,
    Toolbar,
    SaveButton,
} from 'react-admin';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';

import BasicConfigurationForm from './BasicConfigurationForm';
import DatacentersConfigurationForm from './DatacentersConfigurationForm';
import BrokersConfigurationForm from './BrokersConfigurationForm';

const ExperimentForm = (): JSX.Element => (
    <Form>
        <BasicConfigurationForm />
        <DatacentersConfigurationForm />
        <BrokersConfigurationForm />
        <Toolbar>
            <SaveButton
                label='action.run'
                icon={<RocketLaunchIcon />}
            />
        </Toolbar>
    </Form>
);

export default ExperimentForm;
