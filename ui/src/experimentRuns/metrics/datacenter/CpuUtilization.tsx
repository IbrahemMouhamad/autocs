// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import Grid from '@mui/material/Grid';
import DownloadIcon from '@mui/icons-material/Download';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import { useTranslate } from 'react-admin';

import { LinesChart, ChartContainer } from '../../../common';
import {
    groupHistoryByLabels,
    getMaxChartValue,
    getMinChartValue,
} from '../metricUtils';

const CpuResourceAllocation = ({ data }): JSX.Element => {
    const translate = useTranslate();
    const yMax = getMaxChartValue(data);
    const yMin = getMinChartValue(data);

    return (
        <ChartContainer
            headingText='label.run.metrics.cpu.resource_title'
            icon={FileUploadIcon}
        >
            <LinesChart
                data={data}
                xLegend={`${translate('label.run.metrics.time')} (${translate('label.run.metrics.time_unit')})`}
                yLegend={`${translate('label.run.metrics.cpu.percentage')}`}
                yMax={yMax}
                yMin={yMin}
            />
        </ChartContainer>
    );
};

const CpuUsagePercentage = ({ data }): JSX.Element => {
    const translate = useTranslate();
    const yMax = getMaxChartValue(data);
    const yMin = getMinChartValue(data);

    return (
        <ChartContainer
            headingText='label.run.metrics.cpu.usage_title'
            icon={DownloadIcon}
        >
            <LinesChart
                data={data}
                xLegend={`${translate('label.run.metrics.time')} (${translate('label.run.metrics.time_unit')})`}
                yLegend={`${translate('label.run.metrics.cpu.percentage')}`}
                yMax={yMax}
                yMin={yMin}
            />
        </ChartContainer>
    );
};

const CpuUtilization = ({ data }): JSX.Element | null => {
    const translate = useTranslate();
    const upload = groupHistoryByLabels(
        data,
        'time',
        ['allocatedMips'], // upload --allocatedMips , download requestedMips
        {
            allocatedMips: `${translate('label.run.metrics.cpu.allocated')} ${translate('label.run.metrics.cpu.mips_unit')}`,
        },
    );
    const download = groupHistoryByLabels(
        data,
        'time',
        ['requestedMips'], // upload --allocatedMips , download requestedMips
        {
            requestedMips: `${translate('label.run.metrics.cpu.requested')} ${translate('label.run.metrics.cpu.mips_unit')}`,
        },
    );

    return (
        <Grid container>
            <Grid item md={6} sm={12} xs={12}>
                <CpuUsagePercentage data={download} />
            </Grid>
            <Grid item md={6} sm={12} xs={12}>
                <CpuResourceAllocation data={upload} />
            </Grid>
        </Grid>
    );
};

export default CpuUtilization;
