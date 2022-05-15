// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React, { useState } from 'react';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';
import { useRecordContext, useNotify } from 'react-admin';

import { DefaultModal } from '../../common';

import { RunCreate } from '../../experimentRuns';

const RunButton = (): JSX.Element | null => {
    const [run, setRun] = useState(false);
    const record = useRecordContext();
    const notify = useNotify();
    if (!record || record.id == null) {
        return null;
    }
    const { id } = record;

    const onSuccessRun = (): void => {
        notify('ra.notification.created', { messageArgs: { smart_count: 1 } });
        setRun(false);
    };

    return (
        <DefaultModal
            icon={<RocketLaunchIcon />}
            action='label.scenario.action.run'
            open={run}
            setOpen={setRun}
        >
            <RunCreate
                resource='runs'
                scenario={id}
                mutationOptions={{ onSuccess: onSuccessRun }}
            />
        </DefaultModal>
    );
};

export default RunButton;
