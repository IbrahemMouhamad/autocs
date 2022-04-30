// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import clsx from 'clsx';
import { Labeled } from 'react-admin';

const JsonFieldItem = ({
    field,
    record,
    resource,
    ...props
}): JSX.Element => (
    <div
        key={field.props.source}
        className={clsx(`ra-field ra-field-${field.props.source}`, field.props.className)}
        {...props}
    >
        {
            field.props.addLabel &&
            (
                <Labeled
                    resource={resource}
                    label={field.props.label}
                    source={field.props.source}
                >
                    {field}
                </Labeled>
            )
        }
        {
            !field.props.addLabel &&
            (
                typeof field.type === 'string' ? (
                    field
                ) : (
                    React.cloneElement(field, {
                        record,
                        resource,
                    })
                )
            )
        }
    </div>
);

export default JsonFieldItem;
