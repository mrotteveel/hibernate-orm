/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.boot.models.annotations.internal;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.boot.models.HibernateAnnotations;
import org.hibernate.boot.models.annotations.spi.RepeatableContainer;
import org.hibernate.models.spi.ModelsContext;

import static org.hibernate.boot.models.internal.OrmAnnotationHelper.extractJdkValue;

@SuppressWarnings({ "ClassExplicitlyAnnotation", "unused" })
@jakarta.annotation.Generated("org.hibernate.orm.build.annotations.ClassGeneratorProcessor")
public class FetchProfilesAnnotation implements FetchProfiles, RepeatableContainer<FetchProfile> {
	private org.hibernate.annotations.FetchProfile[] value;

	/**
	 * Used in creating dynamic annotation instances (e.g. from XML)
	 */
	public FetchProfilesAnnotation(ModelsContext modelContext) {
	}

	/**
	 * Used in creating annotation instances from JDK variant
	 */
	public FetchProfilesAnnotation(FetchProfiles annotation, ModelsContext modelContext) {
		this.value = extractJdkValue( annotation, HibernateAnnotations.FETCH_PROFILES, "value", modelContext );
	}

	/**
	 * Used in creating annotation instances from Jandex variant
	 */
	public FetchProfilesAnnotation(Map<String, Object> attributeValues, ModelsContext modelContext) {
		this.value = (FetchProfile[]) attributeValues.get( "value" );
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return FetchProfiles.class;
	}

	@Override
	public org.hibernate.annotations.FetchProfile[] value() {
		return value;
	}

	public void value(org.hibernate.annotations.FetchProfile[] value) {
		this.value = value;
	}


}
